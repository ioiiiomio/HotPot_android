package com.cokgyzlar.hotpot.data.profile

import android.util.Log
import retrofit2.HttpException

class ProfileRepositoryImpl (
    private val api : ProfilelApi
) : ProfileRepository{

    override suspend fun getUser(id: Int): UserResult {
        return try{
            val response = api.getUserProfileById(id)
            Log.e("Repository", "success")
            UserResult.Success(response)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            UserResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            UserResult.Error(500, e.message)
        }
    }

    override suspend fun getUser(username: String): UserResult {
        return try{
            val response = api.getUserProfileByUsername(username)
            Log.e("Repository", "success")
            UserResult.Success(response.data)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            UserResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            UserResult.Error(500, e.message)
        }
    }

    override suspend fun getDietician(id: Int): DieticianResult {
        return try{
            val response = api.getDietologById(id)
            Log.e("Repository", "success")
            DieticianResult.Success(response)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            DieticianResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            DieticianResult.Error(500, e.message)
        }
    }

    override suspend fun getDietician(username: String): DieticianResult {
        return try{
            val response = api.getDietologByUsername(username)
            Log.e("Repository", "success")
            DieticianResult.Success(response.data)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            DieticianResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            DieticianResult.Error(500, e.message)
        }
    }

    override suspend fun updateProfile(
        username: String,
        updateRequest: UpdateRequest
    ): UpdateResult {
        return try{
            val response = api.updateProfile(username, updateRequest)
            Log.e("Repository", username)
            Log.e("Repository", updateRequest.toString())
            Log.e("Repository", "success")
            UpdateResult.Success(response.message)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            UpdateResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            UpdateResult.Error(500, e.message)
        }
    }

    override suspend fun getDieticians(): DieticiansResult {
        return try{
            val response = api.getDietologists()
            Log.e("Repository", "success")
            DieticiansResult.Success(response.data)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            DieticiansResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            DieticiansResult.Error(500, e.message)
        }
    }

    override suspend fun getFollows(username: String): FollowsResult {
        return try{
            val response = api.getFollows(username)
            Log.e("Repository", "success")
            FollowsResult.Success(response.data)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            FollowsResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            FollowsResult.Error(500, e.message)
        }
    }

    override suspend fun follow(username: String): UpdateResult {
        return try{
            Log.e("Repository", username)
            val response = api.followAction(FollowRequest("follow", username))
            Log.e("Repository", "success")
            UpdateResult.Success(response.message)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            UpdateResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            UpdateResult.Error(500, e.message)
        }
    }

    override suspend fun unfollow(username: String): UpdateResult {
        return try{
            val response = api.followAction(FollowRequest("unfollow", username))
            Log.e("Repository", "success")
            UpdateResult.Success(response.message)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            UpdateResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            UpdateResult.Error(500, e.message)
        }
    }

    override suspend fun getAppointments(username: String): AppointmentResult {
        return try{
            val response = api.getAppointments(username)
            Log.e("Repository", "success")
            AppointmentResult.Success(response.data)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            AppointmentResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            AppointmentResult.Error(500, e.message)
        }
    }

    override suspend fun createAppointment(
        username: String,
        appt: AppointmentRequest
    ): UpdateResult {
        return try{
            val response = api.createAppointment(username, appt)
            Log.e("Repository", "success")
            UpdateResult.Success(response.message)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            UpdateResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            UpdateResult.Error(500, e.message)
        }
    }
}