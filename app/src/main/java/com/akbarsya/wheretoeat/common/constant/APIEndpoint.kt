package com.akbarsya.wheretoeat.common.constant

object APIEndpoint {
    const val NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org"
    const val NOMINATIM_REVERSE = "/reverse"

    const val CLOUDFLARE_WORKER_BASE_URL = "https://where-to-eat-worker.akbarsya.workers.dev"
    const val CLOUDFLARE_WORKER_USER_BY_FIREBASE = "/users/getByFirebaseUid"
    const val CLOUDFLARE_WORKER_CREATE_NEW_USER = "/users/createNew"
    const val CLOUDFLARE_WORKER_GET_PREFERENCE_TAGS = "/preferenceTags/getAll"
    const val CLOUDFLARE_WORKER_UPDATE_USER = "/users/update"
    const val CLOUDFLARE_WORKER_PLACES_FOR_YOU = "/places/forYou"
    const val CLOUDFLARE_WORKER_PLACES_BY_TAG = "/places/getByTag"
}