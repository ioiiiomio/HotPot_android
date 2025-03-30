package com.prowheelxrassistv01.data
import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import android.util.Base64
import android.util.Log

public class AppStorage private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("HotpotPrefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: AppStorage? = null

        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val ACCESS_TOKEN = "access_token"

        private const val KEY_ALIAS = "Hotpot"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE

        fun getInstance(context: Context): AppStorage {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: AppStorage(context)
                INSTANCE = instance
                instance
            }
        }
    }

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }


    init {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            generateKey()
        }
    }

    fun saveAccessToken(token:String){
        saveData(ACCESS_TOKEN, token)
    }
    fun saveEmail(email: String){
        saveData(EMAIL, email)
    }
    fun savePassword(password: String){
        saveToStorage(PASSWORD, password)
    }
    fun getAccessToken() : String?{
        val accessToken = getData(ACCESS_TOKEN)
        if(accessToken==null){
            Log.e("AppStorage", "Access token was not found")
        }
        return accessToken
    }
    fun getEmail() : String?{
        val mapboxToken =  getData(EMAIL)
        if(mapboxToken==null){
            Log.e("AppStorage", "Email was not found")
        }
        return mapboxToken
    }
    fun getPassword() : String?{
        val mapPosition = retrieveFromStorage(PASSWORD)
        if(mapPosition==null){
            Log.e("AppStorage", "Map position was not found")
        }
        return mapPosition
    }

    /**
     * Encrypt and store the token in the Keystore.
     */
    private fun saveData(key:String, token: String) {
        val cipher = getCipher(Cipher.ENCRYPT_MODE)
        val encryptedData = cipher.doFinal(token.toByteArray())
        val iv = cipher.iv

        // Combine IV and encrypted token, then save as Base64
        val combined = iv + encryptedData
        saveToStorage(key, Base64.encodeToString(combined, Base64.DEFAULT))
    }

    /**
     * Retrieve and decrypt the token from the Keystore.
     */
    private fun getData(key: String): String? {
        val encodedCombined = retrieveFromStorage(key) ?: return null
        val combined = Base64.decode(encodedCombined, Base64.DEFAULT)

        // Extract IV and encrypted token
        val iv = combined.copyOfRange(0, 12)
        val encryptedData = combined.copyOfRange(12, combined.size)

        val cipher = getCipher(Cipher.DECRYPT_MODE, iv)
        return String(cipher.doFinal(encryptedData))
    }


    private fun getCipher(mode: Int, iv: ByteArray? = null): Cipher {
        val cipher = Cipher.getInstance("$ENCRYPTION_ALGORITHM/$BLOCK_MODE/$PADDING")
        val secretKey = getSecretKey()

        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(mode, secretKey)
        } else {
            cipher.init(mode, secretKey, GCMParameterSpec(128, iv))
        }

        return cipher
    }

    private fun getSecretKey(): SecretKey {
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    private fun generateKey() {
        val keyGen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM, ANDROID_KEYSTORE)
        keyGen.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(BLOCK_MODE)
                .setEncryptionPaddings(PADDING)
                .setRandomizedEncryptionRequired(true)
                .build()
        )
        keyGen.generateKey()
    }


    private fun saveToStorage(key: String, data: String) {
        sharedPreferences.edit().putString(key, data).apply()
    }

    private fun retrieveFromStorage(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    /**
     * Clear the stored tokens
     */
    fun clearStorage() {
        sharedPreferences.edit().clear().apply()
    }
}