import os
from dotenv import load_dotenv
from openai import OpenAI

load_dotenv(".env")

OPENAI_API_KEY = os.getenv('OPENAI_API_KEY')

model_config = {
    "frequency_penalty": 0,
    "max_tokens": 2048,
    "presence_penalty": 0,
    "temperature": 0.1,
    "top_p": 1,
    "timeout": 100,
}

client = OpenAI(api_key=OPENAI_API_KEY)

class OpenAIChatLLM:
    def __init__(self, client, model_name, config=model_config):
        self.client = client
        self.model_name = model_name
        self.config = config

    def chat(self, system_prompt, user_prompt, schema=None):
        kwargs = self.config.copy()
        if schema is not None:
            kwargs["response_format"] = schema
        response = self.client.beta.chat.completions.parse(
            model=self.model_name,
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_prompt},
            ],
            **kwargs
        )
        return response.choices[0].message.content

LLM = OpenAIChatLLM(
    client=client,
    model_name="gpt-4.1",
    config=model_config
)
