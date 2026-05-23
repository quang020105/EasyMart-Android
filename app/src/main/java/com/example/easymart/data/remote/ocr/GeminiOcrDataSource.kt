package com.example.easymart.data.remote.ocr

import android.util.Log
import com.example.easymart.BuildConfig
import com.example.easymart.data.remote.api.GeminiApi
import com.example.easymart.data.remote.dto.GeminiProductSuggestion
import com.example.easymart.data.remote.dto.gemini.GeminiContent
import com.example.easymart.data.remote.dto.gemini.GeminiGenerateContentRequest
import com.example.easymart.data.remote.dto.gemini.GeminiGenerationConfig
import com.example.easymart.data.remote.dto.gemini.GeminiPart
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GeminiOcrDataSource @Inject constructor(
    private val gson: Gson,
    private val geminiApi: GeminiApi
) {
    suspend fun normalizeProductText(rawText: String): GeminiProductSuggestion =
        withContext(Dispatchers.IO) {
            Log.d("GeminiOCR", "CALL GEMINI API")
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank()) {
                return@withContext GeminiProductSuggestion()
            }

            // chuẩn hóa nhẹ
            val cleanedText = rawText.replace("\\s+".toRegex(), " ").trim()
            val prompt = buildPrompt(cleanedText)
            val response = geminiApi.generateContent(
                model = "gemini-3.1-flash-lite",
                apiKey = apiKey,
                body = GeminiGenerateContentRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(
                                GeminiPart(
                                    text = prompt
                                )
                            )
                        )
                    ),
                    generationConfig = GeminiGenerationConfig(
                        temperature = 0.2,
                        responseMimeType = "application/json"
                    )
                )
            )
            val responseText = response.candidates
                .first()
                .content
                ?.parts
                ?.first()
                ?.text
                .orEmpty()

            val json = extractJson(responseText)

            if (json.isNullOrBlank()) {
                return@withContext GeminiProductSuggestion()
            }

            return@withContext runCatching {
                gson.fromJson(
                    json,
                    GeminiProductSuggestion::class.java
                )
            }
                .getOrElse { GeminiProductSuggestion() }
                .sanitize()

        }

    private fun buildPrompt(rawText: String): String = """
        Bạn là hệ thống trích xuất thông tin sản phẩm từ OCR cho app bán hàng.

        OCR text bên dưới có thể bị:
        - sai dấu tiếng Việt,
        - nhầm ký tự (O/0, I/l/1, rn/m, v.v.),
        - thiếu ký tự,
        - xuống dòng lung tung,
        - lặp chữ hoặc thừa ký tự.

        Nhiệm vụ:
        - Đọc toàn bộ OCR text và tự suy luận nội dung đúng nhất theo ngữ cảnh.
        - Ưu tiên khôi phục tiếng Việt có dấu nếu có thể.
        - Chỉ trả về JSON hợp lệ, không markdown, không giải thích thêm.
        - Chỉ dùng đúng 4 key: title, category, description, confidence.

        Quy tắc:
        - title: tên sản phẩm / tên sách ngắn gọn, đúng nghĩa nhất theo ngữ cảnh.
        - category: danh mục phù hợp nhất.
        - description: mô tả ngắn 1 câu, tự nhiên, rõ nghĩa.
        - confidence: số từ 0.0 đến 1.0.
        - Nếu không chắc, trả null cho field đó.
        - Không bịa thông tin không có cơ sở từ OCR.
        - Nếu một từ bị OCR sai nhưng ngữ cảnh rất rõ, hãy sửa theo nghĩa đúng.
        - Nếu OCR có nhiều lỗi, hãy ưu tiên hiểu tổng thể thay vì bám chữ từng ký tự.
        - Không đưa thêm key khác ngoài 4 key trên.
        - Output phải là JSON thuần, bắt đầu bằng { và kết thúc bằng }.
        
        OCR TEXT:
        $rawText
        """.trimIndent()

    private fun extractJson(text: String): String? {
        val trimmed = text.trim()
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) return trimmed
        val start = trimmed.indexOf('{')
        val end = trimmed.lastIndexOf('}')
        return if (start >= 0 && end > start) trimmed.substring(start, end + 1) else null
    }
}


