package permissions.dispatcher.test

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.kotlin.verify
import permissions.dispatcher.ktx.Fun
import permissions.dispatcher.ktx.KtxPermissionRequest

class KtxPermissionRequestTest {
    private lateinit var onPermissionDenied: Fun
    private lateinit var requiresPermission: Fun

    @Before
    fun setUp() {
        onPermissionDenied = mock()
        requiresPermission = mock()
    }

    @Test
    fun `PermissionRequest#proceed invokes requiresPermission`() {
        val request = KtxPermissionRequest.create(onPermissionDenied, requiresPermission)
        request.proceed()

        verify(onPermissionDenied, never()).invoke()
        verify(requiresPermission).invoke()
    }

    @Test
    fun `PermissionRequest#cancel invokes onPermissionDenied`() {
        val request = KtxPermissionRequest.create(onPermissionDenied, requiresPermission)
        request.cancel()

        verify(onPermissionDenied).invoke()
        verify(requiresPermission, never()).invoke()
    }
}
