package com.zyrln.relay

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SplitTunnelPrefsTest {
    @Test
    fun normalizeMode_offWhenOnlyModeWithoutPackages() {
        assertEquals(SplitTunnelPrefs.MODE_OFF, SplitTunnelPrefs.normalizeMode(SplitTunnelPrefs.MODE_ONLY, emptySet()))
        assertEquals(SplitTunnelPrefs.MODE_OFF, SplitTunnelPrefs.normalizeMode(SplitTunnelPrefs.MODE_BYPASS, emptySet()))
    }

    @Test
    fun normalizeMode_keepsModeWhenPackagesSelected() {
        assertEquals(
            SplitTunnelPrefs.MODE_ONLY,
            SplitTunnelPrefs.normalizeMode(SplitTunnelPrefs.MODE_ONLY, setOf("com.example.app")),
        )
    }

    @Test
    fun isActive_requiresModeAndPackages() {
        assertFalse(SplitTunnelPrefs.isActive(SplitTunnelPrefs.Config(SplitTunnelPrefs.MODE_OFF, setOf("a"))))
        assertFalse(SplitTunnelPrefs.isActive(SplitTunnelPrefs.Config(SplitTunnelPrefs.MODE_ONLY, emptySet())))
        assertTrue(SplitTunnelPrefs.isActive(SplitTunnelPrefs.Config(SplitTunnelPrefs.MODE_ONLY, setOf("a"))))
    }
}
