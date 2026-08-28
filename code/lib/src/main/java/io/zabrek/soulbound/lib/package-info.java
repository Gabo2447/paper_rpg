/**
 * The library contains useful default implementations for api classes
 * as well as utility classes used within SoulBound.
 */
@DefaultQualifier(value = NotNull.class, locations = {FIELD, PARAMETER, RETURN})
package io.zabrek.soulbound.lib;

import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

import static org.checkerframework.framework.qual.TypeUseLocation.FIELD;
import static org.checkerframework.framework.qual.TypeUseLocation.PARAMETER;
import static org.checkerframework.framework.qual.TypeUseLocation.RETURN;