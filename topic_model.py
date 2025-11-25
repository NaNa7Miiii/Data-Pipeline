from config import config
import umap
from hdbscan import HDBSCAN
from bertopic.representation import LlamaCPP
from bertopic import BERTopic
from bertopic.vectorizers import ClassTfidfTransformer

from huggingface_hub import hf_hub_download
import os
from llama_cpp import Llama
from prompts import model_re_prompt
from dotenv import load_dotenv
from openai import OpenAI

load_dotenv(".env")
OPENAI_API_KEY = os.getenv('OPENAI_API_KEY')
client = OpenAI(api_key=OPENAI_API_KEY)

model_config = {
    "frequency_penalty": 0,
    "max_tokens": 2048,
    "presence_penalty": 0,
    "temperature": 0.1,
    "top_p": 1,
    "timeout": 100,
}

config_topic = config['topic_modelling']

# 1. model for dimensionality reduction
model_dr = umap.UMAP(**config_topic['umap'])

# 2. model for clustering
model_cl = HDBSCAN(prediction_data=True, **config_topic['hdbscan'])

# 3. model for class-based tf-idf
model_ct = ClassTfidfTransformer(**config_topic['ctfidf'])

# 4. model for representation (instead of using local Llama, we use GPT4.1 for faster inference as demo, industrial solution needs to use Azure models for data privacy)
# model_name_or_path = "NousResearch/Meta-Llama-3-8B-Instruct-GGUF"
# model_basename = "Meta-Llama-3-8B-Instruct-Q4_K_M.gguf"
# os.makedirs('model', exist_ok=True)
# path = "model"
# model_path = hf_hub_download(repo_id=model_name_or_path, filename=model_basename, cache_dir=path)
# llm = Llama(model_path=model_path, n_gpu_layers=-1, n_ctx=4096, stop=["Q:", "\n"],verbose=False)
# representation_model = LlamaCPP(model=llm, prompt=model_re_prompt)

representation_model = OpenAI(
    client=client,
    api_key=OPENAI_API_KEY,
    model="gpt-4.1",
    model_config=model_config,
    prompt=model_re_prompt
)

topic_model = BERTopic(
    umap_model=model_dr,
    hdbscan_model=model_cl,
    ctfidf_model=model_ct,
    representation_model=representation_model
)
