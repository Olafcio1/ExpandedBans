package pl.olafcio.expandedbans.main;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public record ChatLock(String reason, String by) {}
