import os
import pandas as pd
import numpy as np

from huggingface_hub import hf_hub_download
from llama_cpp import Llama
from sentence_transformers import SentenceTransformer
from config import config

import re
import collections
from sklearn.metrics.pairwise import cosine_similarity

from openai import OpenAI
from dotenv import load_dotenv
from schemas import AnalysisResponse
import json

from topic_model import *
from llm import *

# LLM deployed for representation model in BERTopic (can be replaced via openai api model for better performance)
def download_model(model_path, base_model, save_dir='model'):
    os.makedirs(save_dir, exist_ok=True)
    model_path = hf_hub_download(repo_id=model_path, filename=base_model, cache_dir=save_dir)
    return model_path

# Here we run all the layers on gpu, alter based on your gpu capability
def load_llama(model_path, n_gpu_layers=-1, n_ctx=4096, stop=None, verbose=False):
    if stop is None:
        stop = ["Q:", "\n"]
    llm = Llama(model_path=model_path, n_gpu_layers=n_gpu_layers, n_ctx=n_ctx, stop=stop, verbose=verbose)
    return llm

# read the customer reviews
def get_column_list(csv_path, column_name):
    df = pd.read_csv(csv_path) # demand is to read the data column with no additional operations, thus python is sufficient 
    return df[column_name].tolist()

# embed the documents using sentence transformer (require an embedding model accommodates the language pt)
def embed_documents(data, model_name='intfloat/multilingual-e5-large-instruct', normalize_embeddings=True, show_progress_bar=True):
    embedding_model = SentenceTransformer(model_name)
    embeddings = embedding_model.encode(
        data,
        normalize_embeddings=normalize_embeddings,
        show_progress_bar=show_progress_bar
    )
    return embeddings

# BERTopic-based topic modelling pipeline
def topic_modelling(
        data,
        topic_model,
        representation_model,
        model_ct,
        embedding_model='intfloat/multilingual-e5-large-instruct',
        html_hierarchy_path="hierarchy.html",
        html_documents_path="hierarchical_documents.html"
    ):
    # 1. embed the documents
    print("Step1: Embedding documents...")
    embeddings = embed_documents(data, embedding_model)

    # 2. fit the topic model
    print("Step2: Fitting topic model...")
    topics, probs = topic_model.fit_transform(data, embeddings)

    # 3. reduce outliers
    print("Step3: Outlier reduction...")
    THRESH = config['topic_modelling']['outlier_reduction']['embedding_thresh']
    if hasattr(topic_model, '_outliers') and topic_model._outliers > 0:
        topics = topic_model.reduce_outliers(
            data, topics, strategy="embeddings",
            embeddings=embeddings, threshold=THRESH
        )
    print(f"Topics after reduction: {len(set(topics))}")

    # 4. update the topic representations
    print('Step4. Finding topic representations...')
    topic_model.update_topics(
        data, topics=topics,
        representation_model=representation_model,
        ctfidf_model=model_ct
    )

    # 5. analyze and visualize the topics
    print("Step5: Analyzing hierarchical topics...")
    hierarchical_topics = topic_model.hierarchical_topics(data)
    fig_hierarchy = topic_model.visualize_hierarchy(hierarchical_topics=hierarchical_topics)
    fig_hierarchy.write_html(html_hierarchy_path)
    print(f"Hierarchy visualization saved to {html_hierarchy_path}")

    reduced_embeddings = model_dr.fit_transform(embeddings)
    fig_docs = topic_model.visualize_hierarchical_documents(
        data, hierarchical_topics, reduced_embeddings=reduced_embeddings
    )
    fig_docs.write_html(html_documents_path)
    print(f"Hierarchical documents visualization saved to {html_documents_path}")
    return topic_model.topic_labels_, topics, probs

def extract_label(text):
    match = re.search(r'_"([^"]+)"', text)
    if match:
        return match.group(1)
    match2 = re.search(r'\d+_(.*)___', text)
    if match2:
        return match2.group(1).strip()
    return text.strip()

def assign_sentiment_to_topics(labels, embedding_model_name, sentiment_words=['negative', 'neutral', 'positive']):
    embedding_model = SentenceTransformer(embedding_model_name)
    # 1. get the topic labels
    labels = [extract_label(v) for v in labels.values()]
    # 2. embed the sentiment words and topic labels
    sentiment_embs = embedding_model.encode(sentiment_words)
    label_embs = embedding_model.encode(labels)
    # 3. compute cosine similarity and assign sentiment
    sentiment_data = collections.defaultdict(list)
    for i, label_emb in enumerate(label_embs):
        sims = cosine_similarity([label_emb], sentiment_embs)[0]
        sentiment = sentiment_words[np.argmax(sims)]
        sentiment_data[sentiment].append((i, labels[i]))
    return sentiment_data

# build user query prompt for negative topcis analysis (neglect positive topics for now, may change based on PRD file)
def build_negative_topics_prompt(
    df,
    sentiment_data,
    TOP_K=10,
    instruction=(
        "Please analyze the following negative topics identified from clustering results. "
        "For each topic, review the representative texts and summarize the main pain points or issues reflected by users. "
        "Provide actionable recommendations for improvement based on your analysis.\n"
        "Each topic includes a topic ID, topic label, and the top representative texts with the highest relevance.\n"
    )
    ):
    representative_docs = {}
    for topic_id, reason in sentiment_data.get('negative', []):
        sub_df = df[df["topic"] == topic_id]
        if not sub_df.empty:
            top_texts = sub_df.sort_values("prob", ascending=False).head(TOP_K)["text"].tolist()
            representative_docs[topic_id] = top_texts
        else:
            representative_docs[topic_id] = ["No representative texts"]

    prompts = []
    for topic_id, reason in sentiment_data.get('negative', []):
        texts = representative_docs[topic_id]
        prompt = (
            f"Topic ID: {topic_id}\n"
            f"Topic Label: {reason}\n"
            f"Representative Texts:\n"
        )
        for i, doc in enumerate(texts, 1):
            prompt += f"{i}. {doc}\n"
        prompt += "\n"
        prompts.append(prompt)

    query_prompt = instruction + "\n" + "\n".join(prompts)
    return query_prompt

# use LLM to generate analysis response based on the topic modelling results
def get_analysis_response(system_prompt, query_prompt, backbone="gpt-4.1", schema=None):
    load_dotenv()
    client = OpenAI(
        api_key=os.getenv("OPENAI_API_KEY")
    )
    llm = OpenAIChatLLM(
        client=client,
        model_name=backbone
    )
    response_content = llm.chat(system_prompt, query_prompt, schema=schema)
    return json.loads(response_content)






