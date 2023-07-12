package com.example.finalattestationreddit.data.network

import java.util.UUID

class AuthQuery {

    /*
     * Sample Auth URL for "Authorization (Implicit grant flow)" (for Reddit "installed apps" type)
     * https://github.com/reddit-archive/reddit/wiki/OAuth2#authorization-implicit-grant-flow
     *
     * https://www.reddit.com/api/v1/authorize
     *      ?client_id=CLIENT_ID
     *      &response_type=TYPE
     *      &state=RANDOM_STRING
     *      &redirect_uri=URI
     *      &scope=SCOPE_STRING
     */

    companion object {
        const val AUTH_URL = "https://www.reddit.com/api/v1/authorize"

        const val PARAM_CLIENT_ID: String = "client_id"

        const val PARAM_RESPONSE_TYPE: String = "response_type"
        const val VAL_RESPONSE_TYPE: String = "token"

        const val PARAM_REDIRECT_URI: String = "redirect_uri"
        const val VAL_REDIRECT_URI: String = "com.example.finalattestationreddit://auth"

        const val PARAM_STATE: String = "state"

        const val PARAM_SCOPE: String = "scope"

        // todo: request only the scopes you need
        const val VAL_SCOPE: String =
            "identity edit flair history modconfig modflair modlog modposts modwiki mysubreddits " +
                    "privatemessages read report save submit subscribe vote wikiedit wikiread"


        const val URI_FRAGMENT_PART_ACCESS_TOKEN = "access_token"

        internal fun generateAuthRequestState(): String {
            return UUID.randomUUID().toString()
        }
    }

}