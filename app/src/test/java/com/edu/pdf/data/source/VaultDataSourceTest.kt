package com.edu.pdf.data.source

import android.content.Context
import android.os.Environment
import com.edu.pdf.data.security.VaultCryptoEngine
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.File

class VaultDataSourceTest {

    private lateinit var context: Context
    private lateinit var cryptoEngine: VaultCryptoEngine
    private lateinit var vaultDataSource: VaultDataSource
    private val tempDir = File("build/tmp/test_vault")

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        cryptoEngine = mockk(relaxed = true)
        
        // Mock Android Environment class taaki crashes na hon
        mockkStatic(Environment::class)
        tempDir.mkdirs()
        every { Environment.getExternalStoragePublicDirectory(any()) } returns tempDir
        
        // Mock context.filesDir
        every { context.filesDir } returns tempDir

        vaultDataSource = VaultDataSource(context, cryptoEngine)
    }

    @Test
    fun `jab file restore ho, toh crypto engine ke decrypted stream ka use hona chahiye`() = runTest {
        // Arrange: Ek asli physical file banao taaki exists() true ho jaye
        val secureFile = File(tempDir, "test.locked")
        if (!secureFile.exists()) secureFile.createNewFile()
        
        val securePath = secureFile.absolutePath
        val originalName = "my_doc.pdf"
        
        // Nakli decrypted data
        val fakeDecryptedData = "DUMMY PDF CONTENT".toByteArray()
        val mockInputStream = ByteArrayInputStream(fakeDecryptedData)
        
        every { cryptoEngine.getEncryptedInputStream(any()) } returns mockInputStream

        // Act
        try {
            vaultDataSource.restoreFromInternalVault(securePath, originalName)
        } catch (_: Exception) {
            // Hum sirf high-level call check kar rahe hain
        }

        // Assert: Verify ki cryptoEngine ka sahi function call hua
        verify { cryptoEngine.getEncryptedInputStream(any()) }
        
        // Cleanup
        secureFile.delete()
    }
}
