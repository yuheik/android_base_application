package com.example.baseapplication

import org.json.JSONObject

class JiraIssue {
    private val id: String
    internal val key: String
    internal val summary: String
    internal val issueType: String
    internal val storyPoint: Float

    constructor(jsonText: String) {
        val rootJson = JSONObject(jsonText)
        id = rootJson.getString("id")
        key = rootJson.getString("key")

        val fields = rootJson.getJSONObject("fields")
        summary = fields.getString("summary")
        issueType = fields.getJSONObject("issuetype").getString("name")
        storyPoint = fields.getString("customfield_10024").toFloat()
    }

    override fun toString(): String {
        return "Jira:\n" +
                "\tid:        '$id'\n" +
                "\tkey:       '$key'\n" +
                "\tsummary:   '$summary'\n" +
                "\tissueType: '$issueType'\n" +
                "\tstoryPoint: $storyPoint"
    }
}
