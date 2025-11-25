from utils import *
from prompts import *
from topic_model import *
from schemas import AnalysisResponse
import json
import pandas as pd

def main():
    print("Starting topic modelling pipeline...")

    # define parameters
    model_path = "NousResearch/Meta-Llama-3-8B-Instruct-GGUF"
    model_basename = "Meta-Llama-3-8B-Instruct-Q4_K_M.gguf"
    data_path = "top_seller_top_product_reviews.csv"

    # top k docs for each topic selected
    TOP_K = 10

    # define representation LLM
    model_path = download_model(
        model_path=model_path,
        base_model=model_basename,
        save_dir='model'
    )
    representation_llm = load_llama(model_path=model_path)
    representation_model = LlamaCPP(model=representation_llm, prompt=model_re_prompt)

    # read the customer reviews
    data = get_column_list(
        csv_path=data_path,
        column_name="review_comment_message"
    )

    # topic modelling
    topic_labels, topics, probs = topic_modelling(
        data=data,
        topic_model=topic_model,
        representation_model=representation_model,
        model_ct=model_ct,
        embedding_model='intfloat/multilingual-e5-large-instruct',
        html_hierarchy_path="hierarchy.html",
        html_documents_path="hierarchical_documents.html"
    )

    sentiment_data = assign_sentiment_to_topics(
        labels=topic_labels,
        embedding_model_name='intfloat/multilingual-e5-large-instruct',
        sentiment_words=['negative', 'neutral', 'positive']
    )

    df = pd.DataFrame({
        "text": data,
        "topic": topics,
        "prob": probs
    })

    # user prompts
    query_prompt = build_negative_topics_prompt(df, sentiment_data, TOP_K=TOP_K)

    # LLM response of analysis
    llm_response = get_analysis_response(system_prompt, query_prompt, backbone="gpt-4.1", schema=AnalysisResponse)

    # save the result in json format
    with open("analysis_response.json", "w", encoding="utf-8") as f:
        json.dump(llm_response, f, ensure_ascii=False, indent=2)

if __name__ == "__main__":
    main()
