# model_re_prompt = ("""
#         You are given a topic represented by the following documents:
#         [DOCUMENTS]
#         The topic is further characterized by these keywords: '[KEYWORDS]'.
#         Please provide a concise summarization phrase that best captures the essence of this topic, using no more than 7 words.
#         Only output the summarization phrase, without any additional explanation.
#         A:
#     """
# )

# Few shot is used here, for general use cases, please consider the trade-off
model_re_prompt = ("""
    You are provided with a set of documents grouped under a specific topic. The topic is characterized by the following keywords: "[KEYWORDS]".  
    Based on the content of the documents and the provided keywords, generate a concise and accurate summarization phrase in English that captures the essence of the topic. The summary must be no longer than 7 words.  
    
    IMPORTANT GUIDELINES:
    1. The summary should be GENERAL, encompassing the overall theme rather than focusing on specific aspects
    2. If keywords include both positive and negative terms, prioritize a balanced representation
    3. Avoid over-emphasizing negative aspects unless they overwhelmingly dominate the documents
    4. Use broad category terms that can include various related discussions
    5. **The summary MUST be in English.**
    
    Ensure that the summary is relevant to the keywords and reflects the main theme of the documents. It should be general enough to encompass all documents under the topic while being specific enough to convey the core idea.
    Failure to comply with the word limit or language requirement will result in termination.  
    Output only the summary phrase.  

    Documents:  
    [DOCUMENTS]  
                   
    ##Examples##:
    ##Example 1##
    Keywords: "defective, broken, not working, poor quality, malfunction"
    Documents: 
    - "The product arrived broken and doesn't work at all"
    - "Stopped working after just 2 days of use"
    - "Very poor quality materials, easily damaged"
    - "Defective unit, returning for refund"
                   
    Summary: Product quality and reliability issues
                   
    ##Example 2##
    Keywords: "delivery, late, slow, shipping, wait time"
    Documents:
    - "Package arrived 2 weeks later than promised"
    - "Extremely slow shipping, took over a month"
    - "Delivery delayed multiple times without notice"
    - "Waiting forever for my order to arrive"
                   
    Summary: Delivery and shipping delays problems
                   
    ##Example 3##
    Keywords: "battery, drain, life, charge, power"
    Documents:
    - "Battery drains too fast, lasts only 1 hour"
    - "Poor battery life compared to advertised"
    - "Won't hold charge overnight, needs constant charging"
    - "Power consumption is excessive and inefficient"

    Summary: Battery life and power management issues
                   
    Summary:

    """
)


system_prompt = """
    You are a professional analyst. Use chain of thoughts reasoning to analyze the following customer feedback.
    For each step, describe your thinking and the action taken.
    At the end, provide a final resolution that includes a summary of main issues, key insights, and actionable suggestions in structured format.
"""
