package io.github.memydb.data.api.services

import io.github.memydb.data.api.ApiResponse
import io.github.memydb.data.pojos.Page
import io.github.memydb.utils.RefreshLiveData
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface NinegagService {

    @GET("/9gag")
    fun getPage(): RefreshLiveData<ApiResponse<Page>>

    @GET("/9gag/page/{id}")
    fun getPage(@Path("id") id: String): RefreshLiveData<ApiResponse<Page>>
}
