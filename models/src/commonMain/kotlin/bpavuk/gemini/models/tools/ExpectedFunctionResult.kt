package bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

interface ExpectedFunctionResult

@Serializable
class EmptyResult : ExpectedFunctionResult
