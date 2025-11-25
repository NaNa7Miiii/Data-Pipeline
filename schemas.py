from typing import List
from pydantic import BaseModel, Field

# implement chain of though schema for topic analysis
class AnalysisResponse(BaseModel):
    class Step(BaseModel):
        description: str = Field(description="Description of the step taken.")
        action: str = Field(description="Action taken to resolve the issue.")
    steps: List[Step]
    class FinalResolution(BaseModel):
        summary: str = Field(description="A concise summary of the main negative feedback themes.")
        insights: List[str] = Field(description="Key insights or patterns discovered from the feedback.")
        suggestions: List[str] = Field(description="Actionable suggestions for improvement.")
    final_resolution: FinalResolution

