package moe.fuqiuluo.unidbg.env.files

fun fetchMemInfo(): ByteArray {
    return """
        MemTotal:        7635456 kB
        MemFree:          121556 kB
        MemAvailable:    2187628 kB
        Buffers:            2920 kB
        Cached:          2066936 kB
        SwapCached:       514472 kB
        Active:          3009688 kB
        Inactive:        2040388 kB
        Active(anon):    2426100 kB
        Inactive(anon):   703232 kB
        Active(file):     583588 kB
        Inactive(file):  1337156 kB
        Unevictable:       86324 kB
        Mlocked:           86320 kB
        SwapTotal:       6291452 kB
        SwapFree:        4186912 kB
        Dirty:               988 kB
        Writeback:             0 kB
        AnonPages:       2767072 kB
        Mapped:           925828 kB
        Shmem:             65056 kB
        KReclaimable:     279508 kB
        Slab:             486780 kB
        SReclaimable:     115268 kB
        SUnreclaim:       371512 kB
        KernelStack:      100528 kB
        ShadowCallStack:   25156 kB
        PageTables:       166196 kB
        NFS_Unstable:          0 kB
        Bounce:                0 kB
        WritebackTmp:          0 kB
        CommitLimit:    10109180 kB
        Committed_AS:   198710388 kB
        VmallocTotal:   262930368 kB
        VmallocUsed:      218320 kB
        VmallocChunk:          0 kB
        Percpu:            11680 kB
        CmaTotal:         196608 kB
        CmaFree:               0 kB
    """.trimIndent().toByteArray()
}