package com.tasbih.digitaltasbih.ui.screens.groups;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.compose.animation.core.AnimationSpecKt;
import androidx.compose.animation.core.EasingKt;
import androidx.compose.animation.core.InfiniteRepeatableSpec;
import androidx.compose.animation.core.InfiniteTransition;
import androidx.compose.animation.core.InfiniteTransitionKt;
import androidx.compose.animation.core.RepeatMode;
import androidx.compose.foundation.BackgroundKt;
import androidx.compose.foundation.BorderKt;
import androidx.compose.foundation.BorderStroke;
import androidx.compose.foundation.BorderStrokeKt;
import androidx.compose.foundation.ClickableKt;
import androidx.compose.foundation.gestures.FlingBehavior;
import androidx.compose.foundation.interaction.MutableInteractionSource;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.BoxKt;
import androidx.compose.foundation.layout.ColumnKt;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.foundation.layout.ColumnScopeInstance;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.PaddingValues;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.layout.SpacerKt;
import androidx.compose.foundation.lazy.LazyDslKt;
import androidx.compose.foundation.lazy.LazyItemScope;
import androidx.compose.foundation.lazy.LazyListScope;
import androidx.compose.foundation.lazy.LazyListState;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.AddKt;
import androidx.compose.material.icons.filled.BookmarkKt;
import androidx.compose.material.icons.filled.CloseKt;
import androidx.compose.material.icons.filled.GroupsKt;
import androidx.compose.material.icons.filled.HistoryKt;
import androidx.compose.material3.AndroidAlertDialog_androidKt;
import androidx.compose.material3.ButtonColors;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.ButtonElevation;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.CardColors;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.CardElevation;
import androidx.compose.material3.CardKt;
import androidx.compose.material3.IconKt;
import androidx.compose.material3.ModalBottomSheetKt;
import androidx.compose.material3.ModalBottomSheetProperties;
import androidx.compose.material3.ProgressIndicatorKt;
import androidx.compose.material3.SheetState;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Applier;
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.ComposableInferredTarget;
import androidx.compose.runtime.ComposableTarget;
import androidx.compose.runtime.ComposablesKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.CompositionLocal;
import androidx.compose.runtime.CompositionLocalMap;
import androidx.compose.runtime.CompositionScopedCoroutineScopeCanceller;
import androidx.compose.runtime.EffectsKt;
import androidx.compose.runtime.IntState;
import androidx.compose.runtime.LongState;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.MutableLongState;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.runtime.SnapshotMutationPolicy;
import androidx.compose.runtime.SnapshotStateKt;
import androidx.compose.runtime.State;
import androidx.compose.runtime.Updater;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.ComposedModifierKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.draw.ClipKt;
import androidx.compose.ui.geometry.Size;
import androidx.compose.ui.graphics.AndroidImageBitmap_androidKt;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.ColorFilter;
import androidx.compose.ui.graphics.ColorKt;
import androidx.compose.ui.graphics.ImageBitmap;
import androidx.compose.ui.graphics.PathEffect;
import androidx.compose.ui.graphics.Shape;
import androidx.compose.ui.graphics.drawscope.DrawScope;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.hapticfeedback.HapticFeedback;
import androidx.compose.ui.hapticfeedback.HapticFeedbackType;
import androidx.compose.ui.layout.LayoutCoordinates;
import androidx.compose.ui.layout.MeasurePolicy;
import androidx.compose.ui.layout.OnGloballyPositionedModifierKt;
import androidx.compose.ui.node.ComposeUiNode;
import androidx.compose.ui.platform.AndroidCompositionLocals_androidKt;
import androidx.compose.ui.platform.CompositionLocalsKt;
import androidx.compose.ui.semantics.Role;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import androidx.compose.ui.unit.TextUnitKt;
import androidx.compose.ui.window.AndroidDialog_androidKt;
import androidx.compose.ui.window.DialogProperties;
import com.tasbih.digitaltasbih.data.BeadMarbleSkin;
import com.tasbih.digitaltasbih.data.PrayerRepository;
import com.tasbih.digitaltasbih.data.TasbihRepository;
import com.tasbih.digitaltasbih.data.groups.CampaignItem;
import com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository;
import com.tasbih.digitaltasbih.data.groups.GroupInviteHelper;
import com.tasbih.digitaltasbih.data.groups.ParticipantItem;
import com.tasbih.digitaltasbih.data.groups.StoredMegaTicket;
import com.tasbih.digitaltasbih.ui.components.AppToast;
import com.tasbih.digitaltasbih.ui.screens.groups.ComposableSingletons;
import com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$CreateGroupZikrScreen$lambda$917$lambda$906$lambda$850$lambda$849$;
import com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$DhikrPickerSheet$lambda$781$lambda$780$lambda$770$lambda$769$;
import com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$DhikrPickerSheet$lambda$781$lambda$780$lambda$779$lambda$778$;
import com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$LocationSelectionDialog$lambda$1029$lambda$1028$lambda$1027$lambda$1026$lambda$1025$;
import com.tasbih.digitaltasbih.util.AdMobRewardedManager;
import com.tasbih.digitaltasbih.util.NetworkHelper;
import com.tasbih.digitaltasbih.util.TasbihAudioEngine;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LiveDhikrScreen.kt */
@Metadata(d1 = {"\u0000¾\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001f\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u001b\n\u0002\u0018\u0002\u001a\u000e\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u0002\u001a\u0017\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007¢\u0006\u0002\u0010\u0013\u001a1\u0010\u0014\u001a\u00020\u00152\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00122\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u001aH\u0007¢\u0006\u0002\u0010\u001b\u001aK\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001e2\b\b\u0002\u0010\u001f\u001a\u00020 2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\u000e\b\u0002\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0002\u0010$\u001a\u009b\u0001\u0010%\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010&\u001a\u00020 2\u0006\u0010'\u001a\u00020 2\b\b\u0002\u0010\u001f\u001a\u00020 2\b\b\u0002\u0010(\u001a\u00020 2\b\b\u0002\u0010\u0019\u001a\u00020\u001a2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\u000e\b\u0002\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\u000e\b\u0002\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0002\u0010/\u001a\u0083\u0001\u00100\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u00101\u001a\u0002022\b\b\u0002\u00103\u001a\u00020\u00122\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\f\u00104\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\f\u00105\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\b\b\u0002\u0010\u001f\u001a\u00020 2\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\b\b\u0002\u00107\u001a\u00020 2\u000e\b\u0002\u00108\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0007¢\u0006\u0002\u00109\u001a?\u0010:\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010(\u001a\u00020 2\b\b\u0002\u0010\u001f\u001a\u00020 2\b\b\u0002\u00103\u001a\u00020\u00122\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0007¢\u0006\u0002\u0010<\u001aM\u0010=\u001a\u00020\u00152\u0006\u0010>\u001a\u00020\u00122\u0006\u0010?\u001a\u00020\u00122\b\b\u0002\u0010@\u001a\u00020\u00042\n\b\u0002\u0010A\u001a\u0004\u0018\u00010B2\b\b\u0002\u0010\u0019\u001a\u00020\u001a2\f\u0010C\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0004\bD\u0010E\u001a#\u0010F\u001a\u00020\u00152\u0006\u0010G\u001a\u00020\u00122\f\u0010C\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0002\u0010H\u001aG\u0010I\u001a\u00020\u00152\u0006\u0010>\u001a\u00020\u00122\b\u0010J\u001a\u0004\u0018\u00010\u00122\u0006\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020 2\b\b\u0002\u0010\u001f\u001a\u00020 2\f\u0010C\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0002\u0010N\u001a\u0019\u0010O\u001a\u00020\u00152\b\b\u0002\u0010P\u001a\u00020\u0004H\u0003¢\u0006\u0004\bQ\u0010R\u001aI\u0010S\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u00101\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020 2\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\u0012\u0010T\u001a\u000e\u0012\u0004\u0012\u00020V\u0012\u0004\u0012\u00020\u00150UH\u0003¢\u0006\u0002\u0010W\u001a)\u0010X\u001a\u00020\u00152\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\f\u0010Y\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0002\u0010Z\u001aM\u0010[\u001a\u00020\u00152\u0006\u0010\\\u001a\u00020 2\u0006\u0010]\u001a\u00020^2\f\u0010_\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\u0012\u0010`\u001a\u000e\u0012\u0004\u0012\u00020^\u0012\u0004\u0012\u00020\u00150U2\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0002\u0010a\u001a/\u0010b\u001a\u00020\u00152\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00150UH\u0003¢\u0006\u0002\u0010c\u001a9\u0010g\u001a\u00020\u00152\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\u0012\u0010h\u001a\u000e\u0012\u0004\u0012\u00020f\u0012\u0004\u0012\u00020\u00150U2\b\b\u0002\u0010\u001f\u001a\u00020 H\u0003¢\u0006\u0002\u0010i\u001aö\u0001\u0010j\u001a\u00020\u00152\u0006\u00101\u001a\u0002022\b\b\u0002\u00103\u001a\u00020\u00122\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182 \u0001\u0010k\u001a\u009b\u0001\u0012\u0013\u0012\u00110\u0012¢\u0006\f\bm\u0012\b\bn\u0012\u0004\b\b(>\u0012\u0013\u0012\u00110\u0012¢\u0006\f\bm\u0012\b\bn\u0012\u0004\b\b(o\u0012\u0013\u0012\u00110^¢\u0006\f\bm\u0012\b\bn\u0012\u0004\b\b(p\u0012\u0013\u0012\u00110 ¢\u0006\f\bm\u0012\b\bn\u0012\u0004\b\b(q\u0012\u0013\u0012\u00110\u0012¢\u0006\f\bm\u0012\b\bn\u0012\u0004\b\b(r\u0012\u0013\u0012\u00110 ¢\u0006\f\bm\u0012\b\bn\u0012\u0004\b\b(s\u0012\u0013\u0012\u00110\u0012¢\u0006\f\bm\u0012\b\bn\u0012\u0004\b\b(t\u0012\u0004\u0012\u00020\u00150l2\f\u0010u\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\b\b\u0002\u0010\u001f\u001a\u00020 2\f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0002\u0010v\u001a#\u0010w\u001a\u00020\u00152\u0006\u00101\u001a\u0002022\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0007¢\u0006\u0002\u0010x\u001aE\u0010y\u001a\u00020\u00152\u0006\u0010>\u001a\u00020\u00122\f\u0010z\u001a\b\u0012\u0004\u0012\u00020\u00120e2\u0012\u0010h\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00150U2\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0007¢\u0006\u0002\u0010{\u001a1\u0010|\u001a\u00020\u00152\u0006\u0010}\u001a\u00020\u00122\f\u0010~\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0002\u0010\u007f\u001a>\u0010\u0080\u0001\u001a\u00020\u00152\u0006\u0010}\u001a\u00020\u00122\b\b\u0002\u0010\u001f\u001a\u00020 2\r\u0010\u0081\u0001\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0003\u0010\u0082\u0001\u001a<\u0010\u0083\u0001\u001a\u00020\u00152\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\t\b\u0002\u0010\u0084\u0001\u001a\u00020\u00122\u0006\u00101\u001a\u0002022\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0003\u0010\u0085\u0001\u001a$\u0010\u0086\u0001\u001a\u00020\u00152\u0006\u0010>\u001a\u00020\u00122\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00150\u0018H\u0003¢\u0006\u0002\u0010H\"\u0010\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0005\"\u0010\u0010\u0006\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0005\"\u0010\u0010\u0007\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0005\"\u0010\u0010\b\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0005\"\u0010\u0010\t\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0005\"\u0010\u0010\n\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0005\"\u0010\u0010\u000b\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0005\"\u0010\u0010\f\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0005\"\u0010\u0010\r\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0005\"\u0010\u0010\u000e\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0005\"\u0014\u0010d\u001a\b\u0012\u0004\u0012\u00020f0eX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0087\u0001²\u0006\n\u0010\u001f\u001a\u00020 X\u008a\u008e\u0002²\u0006\n\u00103\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010\u0088\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010\u0089\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u0011\u0010\u008a\u0001\u001a\b\u0012\u0004\u0012\u00020\u001e0eX\u008a\u008e\u0002²\u0006\u0011\u0010\u008b\u0001\u001a\b\u0012\u0004\u0012\u00020\u001e0eX\u008a\u008e\u0002²\u0006\f\u0010\u008c\u0001\u001a\u00030\u008d\u0001X\u008a\u008e\u0002²\u0006\f\u0010\u008e\u0001\u001a\u00030\u008d\u0001X\u008a\u008e\u0002²\u0006\u0012\u0010\u008f\u0001\u001a\t\u0012\u0004\u0012\u00020\u00120\u0090\u0001X\u008a\u008e\u0002²\u0006\u0012\u0010\u0091\u0001\u001a\t\u0012\u0004\u0012\u00020\u00120\u0090\u0001X\u008a\u008e\u0002²\u0006\u000b\u0010\u0092\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\r\u0010\u0093\u0001\u001a\u0004\u0018\u00010\u001eX\u008a\u008e\u0002²\u0006\u000b\u0010\u0094\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010\u0095\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010\u0096\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010\u0097\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010\u0098\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\r\u0010\u0099\u0001\u001a\u0004\u0018\u00010\u001eX\u008a\u008e\u0002²\u0006\r\u0010\u009a\u0001\u001a\u0004\u0018\u00010\u001eX\u008a\u008e\u0002²\u0006\r\u0010\u009b\u0001\u001a\u0004\u0018\u00010\u0012X\u008a\u008e\u0002²\u0006\r\u0010\u009c\u0001\u001a\u0004\u0018\u00010\u001eX\u008a\u008e\u0002²\u0006\u001a\u0010\u009d\u0001\u001a\u0011\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020 \u0018\u00010\u009e\u0001X\u008a\u008e\u0002²\u0006\r\u0010\u009f\u0001\u001a\u0004\u0018\u00010\u001eX\u008a\u008e\u0002²\u0006\r\u0010 \u0001\u001a\u0004\u0018\u00010\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010¡\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\f\u0010¢\u0001\u001a\u00030\u008d\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010£\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010¥\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010¦\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010§\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010¨\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000b\u0010©\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\f\u0010ª\u0001\u001a\u00030\u008d\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010«\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010¬\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010\u00ad\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010®\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010¯\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010°\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000b\u0010±\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010²\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010³\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010\u009b\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010´\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\f\u0010µ\u0001\u001a\u00030\u008d\u0001X\u008a\u008e\u0002²\u0006\f\u0010¶\u0001\u001a\u00030\u008d\u0001X\u008a\u008e\u0002²\u0006\u000b\u0010·\u0001\u001a\u00020^X\u008a\u008e\u0002²\u0006\u000b\u0010¸\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010¹\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010º\u0001\u001a\u00020^X\u008a\u008e\u0002²\u0006\u000b\u0010»\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010¼\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010½\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010¾\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\f\u0010¿\u0001\u001a\u00030À\u0001X\u008a\u008e\u0002²\u0006\u000b\u0010Á\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010Â\u0001\u001a\u00020^X\u008a\u008e\u0002²\u0006\f\u0010Ã\u0001\u001a\u00030\u008d\u0001X\u008a\u008e\u0002²\u0006\u000b\u0010Ä\u0001\u001a\u00020^X\u008a\u008e\u0002²\u0006\u000b\u0010Å\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\f\u0010Æ\u0001\u001a\u00030Ç\u0001X\u008a\u0084\u0002²\u0006\n\u0010n\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010È\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\n\u0010r\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010É\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010Ê\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010¸\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010Ë\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\r\u0010Ì\u0001\u001a\u0004\u0018\u00010\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010Í\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010Î\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010Ï\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010Ð\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\f\u0010Ñ\u0001\u001a\u00030\u008d\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010Ò\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010Ó\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010Ô\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010Õ\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\u000e\u0010Ö\u0001\u001a\u0005\u0018\u00010¤\u0001X\u008a\u008e\u0002²\u0006\n\u0010>\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\n\u0010o\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010×\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010Ø\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\n\u0010t\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\n\u0010q\u001a\u00020 X\u008a\u008e\u0002²\u0006\n\u0010s\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010Ù\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010Ú\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010Û\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010Ü\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010Ý\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010Þ\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\n\u0010r\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u000b\u0010Ê\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010ß\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010à\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010á\u0001\u001a\u00020 X\u008a\u008e\u0002²\u0006\u000b\u0010Î\u0001\u001a\u00020\u0012X\u008a\u008e\u0002²\u0006\u0012\u0010â\u0001\u001a\t\u0012\u0005\u0012\u00030ã\u00010eX\u008a\u008e\u0002²\u0006\u000b\u0010\u0092\u0001\u001a\u00020 X\u008a\u008e\u0002"}, d2 = {"findActivity", "Landroid/app/Activity;", "Landroid/content/Context;", "DeepEmeraldGradTop", "Landroidx/compose/ui/graphics/Color;", "J", "DeepEmeraldGradMid", "DeepEmeraldGradEnd", "LuxuryGold", "BrightGold", "ShimmerGold", "ParchmentGold", "DarkCardBg", "DarkCardBorder", "TextMutedGreen", "rememberAssetBitmap", "Landroidx/compose/ui/graphics/ImageBitmap;", "path", "", "(Ljava/lang/String;Landroidx/compose/runtime/Composer;I)Landroidx/compose/ui/graphics/ImageBitmap;", "LiveDhikrScreen", "", "initialAutoJoinCode", "onBack", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "(Ljava/lang/String;Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "GrandUmrahMegaPoster", "campaign", "Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;", "isDarkTheme", "", "onJoinClick", "onRegisterClick", "onViewParticipants", "(Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;ZLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;II)V", "CampaignCard", "isJoined", "isSaved", "isCreator", "onEnter", "onToggleSave", "onJoin", "onShare", "onViewLeaderboard", "onDelete", "(Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;ZZZZLandroidx/compose/ui/Modifier;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;III)V", "LiveActiveDhikrRoom", "repository", "Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;", "selectedLanguage", "onOpenLeaderboard", "onOpenFazilat", "onToggleTheme", "initiallyShowShareModal", "onDismissShareModal", "(Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;Ljava/lang/String;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;ZLkotlin/jvm/functions/Function0;ZLkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;II)V", "GroupShareModal", "onDismiss", "(Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;ZZLjava/lang/String;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;II)V", "SharePlatformTile", "title", "iconText", "bgColor", "bgBrush", "Landroidx/compose/ui/graphics/Brush;", "onClick", "SharePlatformTile-FU0evQE", "(Ljava/lang/String;Ljava/lang/String;JLandroidx/compose/ui/graphics/Brush;Landroidx/compose/ui/Modifier;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;II)V", "ShareIconBtn", "platform", "(Ljava/lang/String;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "PillButton", "iconPath", "fallbackIcon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "isSelected", "(Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;ZZLkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;II)V", "PulsingLiveDot", "color", "PulsingLiveDot-Iv8Zu3U", "(JLandroidx/compose/runtime/Composer;II)V", "UmrahMegaRegisterModal", "onSuccess", "Lkotlin/Function1;", "Lcom/tasbih/digitaltasbih/data/groups/StoredMegaTicket;", "(Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;ZLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;II)V", "NoInternetConnectionDialog", "onRetry", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "SmartAutoCounterModal", "isRunning", "currentSpeedMs", "", "onToggle", "onSelectSpeed", "(ZJLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "JoinByCodeDialog", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "FULL_DHIKR_CATALOG", "", "Lcom/tasbih/digitaltasbih/ui/screens/groups/DhikrCatalogItem;", "DhikrPickerSheet", "onSelect", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;ZLandroidx/compose/runtime/Composer;II)V", "CreateGroupZikrScreen", "onCreate", "Lkotlin/Function7;", "Lkotlin/ParameterName;", "name", "arabic", "target", "isPrivate", "city", "isSpecialMega", "note", "onOpenAdmin", "(Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;Ljava/lang/String;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function7;Lkotlin/jvm/functions/Function0;ZLkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;II)V", "WelcomeToGroupCampaignsDialog", "(Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "LocationSelectionDialog", "items", "(Ljava/lang/String;Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "LeaderboardAdPromptDialog", "campaignTitle", "onWatchAd", "(Ljava/lang/String;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "DeleteGroupConfirmDialog", "onWatchAdAndDelete", "(Ljava/lang/String;ZLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;II)V", "LeaderboardModalSheet", "campaignId", "(Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;Ljava/lang/String;Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;II)V", "FazilatModalSheet", "app", "isMoreLangOpen", "selectedTab", "campaigns", "specialCampaigns", "liveCount", "", "completedCount", "joinedIds", "", "savedIds", "isLoading", "activeCampaign", "showCreateModal", "showJoinCodeModal", "showMegaRegisterModal", "showNoInternetDialog", "showProfileModal", "showLeaderboardCampaign", "pendingAdCampaign", "showFazilatModal", "createdShareCampaign", "shareModalCampaign", "Lkotlin/Pair;", "campaignToDelete", "handledAutoJoinCode", "isMainTourOpen", "mainTourStep", "rootCoords", "Landroidx/compose/ui/layout/LayoutCoordinates;", "statsRowCoords", "tabsRowCoords", "actionRowCoords", "firstCardCoords", "isRoomTourOpen", "roomTourStep", "roomRootCoords", "infoCardCoords", "dialCoords", "autoControlCoords", "leaderboardBarCoords", "submitBtnCoords", "showShareModal", "showLeaderboardModal", "showAdPrompt", "showDeleteConfirmModal", "pendingCount", "sessionCount", "liveTotal", "isSubmitting", "isAutoRunning", "autoSpeedMs", "showAutoModal", "isSoundOn", "isVibrateOn", "isBeadsViewMode", "activeSkin", "Lcom/tasbih/digitaltasbih/data/BeadMarbleSkin;", "showMarbleGalleryModal", "lastTapTimestamp", "hourlyCount", "hourBucketStart", "cheatWarningShown", "alpha", "", "phone", "agreeChecked", "consentChecked", "consentError", "generalErrorMsg", "code", "searchQuery", "selectedCat", "isCreateTourOpen", "createTourStep", "createRootCoords", "chooseZikrCoords", "targetCoords", "privacyCoords", "createBtnCoords", "meaning", "targetStr", "showPicker", "selectedLang", "firstName", "lastName", "country", "state", "showCountryMenu", "showStateMenu", "showCityMenu", "participants", "Lcom/tasbih/digitaltasbih/data/groups/ParticipantItem;"}, k = 2, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nLiveDhikrScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LiveDhikrScreen.kt\ncom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt\n+ 2 CompositionLocal.kt\nandroidx/compose/runtime/CompositionLocal\n+ 3 Composer.kt\nandroidx/compose/runtime/ComposerKt\n+ 4 Effects.kt\nandroidx/compose/runtime/EffectsKt\n+ 5 Effects.kt\nandroidx/compose/runtime/EffectsKt$rememberCoroutineScope$1\n+ 6 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 7 Box.kt\nandroidx/compose/foundation/layout/BoxKt\n+ 8 Layout.kt\nandroidx/compose/ui/layout/LayoutKt\n+ 9 Composables.kt\nandroidx/compose/runtime/ComposablesKt\n+ 10 Composer.kt\nandroidx/compose/runtime/Updater\n+ 11 Column.kt\nandroidx/compose/foundation/layout/ColumnKt\n+ 12 Dp.kt\nandroidx/compose/ui/unit/DpKt\n+ 13 Row.kt\nandroidx/compose/foundation/layout/RowKt\n+ 14 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 15 SnapshotState.kt\nandroidx/compose/runtime/SnapshotStateKt__SnapshotStateKt\n+ 16 SnapshotIntState.kt\nandroidx/compose/runtime/SnapshotIntStateKt__SnapshotIntStateKt\n+ 17 LazyDsl.kt\nandroidx/compose/foundation/lazy/LazyDslKt\n+ 18 SnapshotLongState.kt\nandroidx/compose/runtime/SnapshotLongStateKt__SnapshotLongStateKt\n+ 19 _Strings.kt\nkotlin/text/StringsKt___StringsKt\n*L\n1#1,6821:1\n77#2:6822\n77#2:6829\n77#2:6830\n77#2:7436\n77#2:7437\n77#2:8908\n77#2:8909\n77#2:9104\n77#2:9105\n77#2:9775\n1225#3,6:6823\n1225#3,3:6836\n1228#3,3:6842\n1225#3,6:6846\n1225#3,6:6852\n1225#3,6:6858\n1225#3,6:6864\n1225#3,6:6870\n1225#3,6:6876\n1225#3,6:6882\n1225#3,6:6888\n1225#3,6:6894\n1225#3,6:6900\n1225#3,6:6906\n1225#3,6:6912\n1225#3,6:6918\n1225#3,6:6924\n1225#3,6:6930\n1225#3,6:6936\n1225#3,6:6942\n1225#3,6:6948\n1225#3,6:6954\n1225#3,6:6960\n1225#3,6:6966\n1225#3,6:6972\n1225#3,6:6978\n1225#3,6:6984\n1225#3,6:6990\n1225#3,3:6996\n1228#3,3:7003\n1225#3,6:7006\n1225#3,6:7012\n1225#3,6:7018\n1225#3,6:7024\n1225#3,6:7030\n1225#3,6:7036\n1225#3,6:7042\n1225#3,6:7048\n1225#3,6:7054\n1225#3,6:7060\n1225#3,6:7066\n1225#3,6:7072\n1225#3,6:7078\n1225#3,6:7084\n1225#3,6:7090\n1225#3,6:7096\n1225#3,6:7102\n1225#3,6:7108\n1225#3,6:7114\n1225#3,6:7120\n1225#3,6:7126\n1225#3,6:7206\n1225#3,6:7212\n1225#3,6:7218\n1225#3,6:7226\n1225#3,6:7236\n1225#3,6:7242\n1225#3,6:7248\n1225#3,6:7254\n1225#3,6:7260\n1225#3,6:7266\n1225#3,6:7272\n1225#3,6:7278\n1225#3,6:7284\n1225#3,6:7290\n1225#3,6:7296\n1225#3,6:7302\n1225#3,6:7308\n1225#3,6:7314\n1225#3,6:7320\n1225#3,6:7326\n1225#3,6:7332\n1225#3,6:7338\n1225#3,6:7344\n1225#3,6:7350\n1225#3,6:7356\n1225#3,6:7362\n1225#3,6:7368\n1225#3,6:7374\n1225#3,6:7384\n1225#3,6:7393\n1225#3,6:7401\n1225#3,6:7407\n1225#3,6:7413\n1225#3,6:7422\n1225#3,6:7430\n1225#3,3:7443\n1228#3,3:7449\n1225#3,6:7453\n1225#3,6:7459\n1225#3,6:7465\n1225#3,6:7471\n1225#3,6:7477\n1225#3,6:7483\n1225#3,6:7489\n1225#3,6:7495\n1225#3,6:7501\n1225#3,6:7507\n1225#3,6:7513\n1225#3,6:7519\n1225#3,6:7525\n1225#3,6:7531\n1225#3,6:7537\n1225#3,6:7543\n1225#3,6:7549\n1225#3,6:7555\n1225#3,6:7561\n1225#3,6:7567\n1225#3,6:7573\n1225#3,6:7579\n1225#3,6:7585\n1225#3,6:7591\n1225#3,6:7597\n1225#3,6:7603\n1225#3,6:7609\n1225#3,6:7615\n1225#3,6:7621\n1225#3,6:7627\n1225#3,6:7633\n1225#3,6:7639\n1225#3,6:7645\n1225#3,6:7651\n1225#3,6:7657\n1225#3,6:7663\n1225#3,6:7669\n1225#3,6:7675\n1225#3,6:7681\n1225#3,6:7687\n1225#3,6:7838\n1225#3,6:7931\n1225#3,6:7937\n1225#3,6:7943\n1225#3,6:7949\n1225#3,6:7965\n1225#3,6:7978\n1225#3,6:8019\n1225#3,6:8059\n1225#3,6:8101\n1225#3,6:8113\n1225#3,6:8242\n1225#3,6:8293\n1225#3,6:8339\n1225#3,6:8345\n1225#3,6:8351\n1225#3,6:8367\n1225#3,6:8375\n1225#3,6:8586\n1225#3,6:8631\n1225#3,6:8814\n1225#3,6:8820\n1225#3,6:8826\n1225#3,6:8832\n1225#3,6:8838\n1225#3,6:8844\n1225#3,6:8850\n1225#3,6:8856\n1225#3,6:8862\n1225#3,6:8868\n1225#3,6:8874\n1225#3,6:8880\n1225#3,6:8886\n1225#3,6:8892\n1225#3,6:8898\n1225#3,6:8911\n1225#3,6:8919\n1225#3,6:9012\n1225#3,3:9111\n1228#3,3:9117\n1225#3,6:9121\n1225#3,6:9127\n1225#3,6:9133\n1225#3,6:9139\n1225#3,6:9145\n1225#3,6:9151\n1225#3,6:9157\n1225#3,6:9163\n1225#3,6:9171\n1225#3,6:9177\n1225#3,6:9183\n1225#3,6:9190\n1225#3,6:9196\n1225#3,6:9202\n1225#3,6:9208\n1225#3,6:9214\n1225#3,6:9220\n1225#3,6:9226\n1225#3,6:9232\n1225#3,6:9238\n1225#3,6:9244\n1225#3,6:9250\n1225#3,6:9256\n1225#3,6:9262\n1225#3,6:9268\n1225#3,6:9274\n1225#3,6:9280\n1225#3,6:9286\n1225#3,6:9292\n1225#3,6:9298\n1225#3,6:9304\n1225#3,6:9310\n1225#3,6:9547\n1225#3,6:9564\n1225#3,6:9572\n1225#3,6:9585\n1225#3,6:9598\n1225#3,6:9644\n1225#3,6:9741\n1225#3,6:9747\n1225#3,6:9753\n1225#3,6:9759\n1225#3,6:9765\n1225#3,6:9776\n1225#3,6:9782\n1225#3,6:9788\n1225#3,6:9794\n1225#3,6:9800\n1225#3,6:9806\n1225#3,6:9812\n1225#3,6:9818\n1225#3,6:9824\n1225#3,6:9830\n1225#3,6:9836\n1225#3,6:9842\n1225#3,6:9848\n1225#3,6:9854\n1225#3,6:9860\n1225#3,6:9866\n1225#3,6:9872\n1225#3,6:9878\n1225#3,6:9884\n1225#3,3:9890\n1228#3,3:9896\n1225#3,6:9899\n1225#3,6:9905\n1225#3,6:9911\n1225#3,6:9917\n1225#3,6:9923\n1225#3,6:10231\n1225#3,6:10278\n1225#3,6:10326\n1225#3,6:10379\n1225#3,6:10424\n1225#3,6:10558\n1225#3,6:10693\n1225#3,6:10699\n1225#3,6:10705\n1225#3,6:10711\n1225#3,6:10720\n1225#3,6:10809\n1225#3,6:10816\n1225#3,6:10822\n1225#3,6:10867\n1225#3,6:11086\n1225#3,6:11264\n1225#3,6:11270\n1225#3,6:11276\n1225#3,6:11762\n1225#3,6:11812\n1225#3,6:11865\n1225#3,6:12755\n1225#3,6:12961\n1225#3,6:13051\n1225#3,6:13099\n1225#3,6:13147\n1225#3,6:13410\n1225#3,6:13578\n1225#3,6:13829\n1225#3,6:13836\n1225#3,6:14049\n1225#3,6:14095\n1225#3,6:14141\n1225#3,6:14187\n1225#3,6:14233\n1225#3,6:14280\n1225#3,6:14332\n1225#3,6:14863\n1225#3,6:15028\n1225#3,6:15085\n1225#3,6:15091\n1225#3,6:15097\n1225#3,6:15144\n1225#3,6:15150\n1225#3,6:15156\n1225#3,6:15290\n1225#3,6:15375\n1225#3,6:15382\n1225#3,6:15474\n1225#3,6:15643\n1225#3,6:15686\n1225#3,6:15701\n1225#3,6:15818\n1225#3,6:15844\n1225#3,6:15929\n1225#3,6:15947\n1225#3,6:15991\n1225#3,6:16106\n1225#3,6:16114\n1225#3,6:16126\n1225#3,6:16405\n1225#3,6:16615\n1225#3,6:17008\n1225#3,6:17055\n1225#3,6:17109\n1225#3,6:17117\n1225#3,6:17162\n1225#3,6:17211\n1225#3,6:17643\n1225#3,6:17733\n1225#3,6:17832\n1225#3,6:18027\n1225#3,6:18033\n1225#3,6:18160\n1225#3,6:18166\n1225#3,6:18261\n1225#3,6:18318\n1225#3,6:18461\n1225#3,6:18472\n1225#3,6:20549\n481#4:6831\n480#4,4:6832\n484#4,2:6839\n488#4:6845\n481#4:7438\n480#4,4:7439\n484#4,2:7446\n488#4:7452\n481#4:9106\n480#4,4:9107\n484#4,2:9114\n488#4:9120\n480#5:6841\n480#5:7448\n480#5:9116\n295#6,2:6999\n295#6,2:7001\n774#6:9893\n865#6,2:9894\n774#6:11324\n865#6,2:11325\n774#6:11327\n865#6,2:11328\n774#6:11330\n865#6,2:11331\n774#6:11333\n865#6,2:11334\n774#6:11336\n865#6,2:11337\n774#6:11339\n865#6,2:11340\n774#6:11342\n865#6,2:11343\n1869#6:15926\n1870#6:15935\n774#6:16121\n865#6,2:16122\n1869#6:17051\n1870#6:17101\n71#7:7132\n68#7,6:7133\n74#7:7167\n78#7:7383\n71#7:7693\n68#7,6:7694\n74#7:7728\n71#7:7766\n68#7,6:7767\n74#7:7801\n71#7:7848\n68#7,6:7849\n74#7:7883\n78#7:7930\n78#7:7962\n71#7:7984\n69#7,5:7985\n74#7:8018\n71#7:8065\n69#7,5:8066\n74#7:8099\n78#7:8110\n71#7:8120\n68#7,6:8121\n74#7:8155\n78#7:8201\n71#7:8207\n69#7,5:8208\n74#7:8241\n78#7:8251\n78#7:8364\n71#7:8382\n68#7,6:8383\n74#7:8417\n78#7:8583\n71#7:8592\n68#7,6:8593\n74#7:8627\n71#7:8638\n69#7,5:8639\n74#7:8672\n78#7:8796\n78#7:8800\n78#7:8907\n71#7:8926\n69#7,5:8927\n74#7:8960\n78#7:9006\n71#7:9019\n68#7,6:9020\n74#7:9054\n78#7:9102\n71#7:9316\n68#7,6:9317\n74#7:9351\n71#7:9390\n68#7,6:9391\n74#7:9425\n71#7:9466\n69#7,5:9467\n74#7:9500\n78#7:9505\n78#7:9560\n71#7:9604\n68#7,6:9605\n74#7:9639\n71#7:9650\n69#7,5:9651\n74#7:9684\n78#7:9731\n78#7:9735\n78#7:9774\n71#7:10057\n69#7,5:10058\n74#7:10091\n78#7:10096\n71#7:10147\n68#7,6:10148\n74#7:10182\n78#7:10186\n71#7:10237\n69#7,5:10238\n74#7:10271\n78#7:10275\n71#7:10284\n69#7,5:10285\n74#7:10318\n78#7:10323\n71#7:10332\n69#7,5:10333\n74#7:10366\n78#7:10370\n71#7:10431\n68#7,6:10432\n74#7:10466\n78#7:10554\n71#7:10565\n68#7,6:10566\n74#7:10600\n78#7:10688\n71#7:10727\n68#7,6:10728\n74#7:10762\n78#7:10808\n71#7:10874\n68#7,6:10875\n74#7:10909\n71#7:11035\n69#7,5:11036\n74#7:11069\n78#7:11074\n78#7:11082\n71#7:11093\n68#7,6:11094\n74#7:11128\n71#7:11212\n69#7,5:11213\n74#7:11246\n78#7:11251\n78#7:11259\n71#7:11467\n68#7,6:11468\n74#7:11502\n78#7:11506\n71#7:11510\n68#7,6:11511\n74#7:11545\n78#7:11549\n71#7:11557\n68#7,6:11558\n74#7:11592\n78#7:11596\n71#7:11649\n68#7,6:11650\n74#7:11684\n78#7:11777\n71#7:11872\n68#7,6:11873\n74#7:11907\n78#7:11911\n71#7:12033\n68#7,6:12034\n74#7:12068\n78#7:12114\n71#7:12119\n68#7,6:12120\n74#7:12154\n78#7:12158\n71#7:12163\n68#7,6:12164\n74#7:12198\n78#7:12202\n71#7:12387\n69#7,5:12388\n74#7:12421\n78#7:12467\n71#7:12473\n68#7,6:12474\n74#7:12508\n71#7:12666\n68#7,6:12667\n74#7:12701\n78#7:12706\n78#7:12751\n71#7:12762\n68#7,6:12763\n74#7:12797\n78#7:12879\n71#7:12884\n68#7,6:12885\n74#7:12919\n78#7:12923\n71#7:12967\n69#7,5:12968\n74#7:13001\n78#7:13048\n71#7:13057\n69#7,5:13058\n74#7:13091\n78#7:13096\n71#7:13105\n69#7,5:13106\n74#7:13139\n78#7:13144\n71#7:13153\n69#7,5:13154\n74#7:13187\n78#7:13192\n71#7:13289\n69#7,5:13290\n74#7:13323\n78#7:13327\n71#7:13329\n69#7,5:13330\n74#7:13363\n78#7:13368\n71#7:13417\n68#7,6:13418\n74#7:13452\n78#7:13499\n71#7:13536\n68#7,6:13537\n74#7:13571\n78#7:13575\n71#7:13668\n68#7,6:13669\n74#7:13703\n71#7:13744\n68#7,6:13745\n74#7:13779\n78#7:13783\n78#7:13791\n71#7:13970\n68#7,6:13971\n74#7:14005\n78#7:14009\n71#7:14055\n69#7,5:14056\n74#7:14089\n78#7:14093\n71#7:14101\n69#7,5:14102\n74#7:14135\n78#7:14139\n71#7:14147\n69#7,5:14148\n74#7:14181\n78#7:14185\n71#7:14193\n69#7,5:14194\n74#7:14227\n78#7:14231\n71#7:14239\n69#7,5:14240\n74#7:14273\n78#7:14277\n71#7:14286\n69#7,5:14287\n74#7:14320\n78#7:14324\n71#7:14338\n69#7,5:14339\n74#7:14372\n78#7:14419\n71#7:14462\n69#7,5:14463\n74#7:14496\n78#7:14500\n71#7:14506\n68#7,6:14507\n74#7:14541\n71#7:14612\n68#7,6:14613\n74#7:14647\n78#7:14651\n71#7:14654\n68#7,6:14655\n74#7:14689\n78#7:14693\n78#7:14747\n71#7:14753\n68#7,6:14754\n74#7:14788\n78#7:14876\n71#7:14882\n68#7,6:14883\n74#7:14917\n78#7:15046\n71#7:15169\n68#7,6:15170\n74#7:15204\n78#7:15250\n71#7:15391\n68#7,6:15392\n74#7:15426\n78#7:15472\n71#7:15483\n68#7,6:15484\n74#7:15518\n71#7:15591\n68#7,6:15592\n74#7:15626\n78#7:15630\n78#7:15642\n71#7:15708\n68#7,6:15709\n74#7:15743\n78#7:15838\n71#7:16187\n69#7,5:16188\n74#7:16221\n78#7:16226\n71#7:16316\n69#7,5:16317\n74#7:16350\n78#7:16354\n71#7:16412\n68#7,6:16413\n74#7:16447\n71#7:16519\n69#7,5:16520\n74#7:16553\n78#7:16557\n78#7:16611\n71#7:16625\n69#7,5:16626\n74#7:16659\n71#7:16698\n69#7,5:16699\n74#7:16732\n78#7:16736\n71#7:16785\n68#7,6:16786\n74#7:16820\n78#7:16824\n78#7:16832\n71#7:16920\n69#7,5:16921\n74#7:16954\n78#7:16958\n71#7:17062\n69#7,5:17063\n74#7:17096\n78#7:17100\n71#7:17169\n69#7,5:17170\n74#7:17203\n78#7:17207\n71#7:17218\n69#7,5:17219\n74#7:17252\n78#7:17256\n71#7:17446\n69#7,5:17447\n74#7:17480\n78#7:17485\n71#7:17608\n69#7,5:17609\n74#7:17642\n78#7:17652\n71#7:17698\n69#7,5:17699\n74#7:17732\n78#7:17742\n71#7:17839\n69#7,5:17840\n74#7:17873\n78#7:17914\n71#7:17960\n69#7,5:17961\n74#7:17994\n78#7:18047\n71#7:18093\n69#7,5:18094\n74#7:18127\n78#7:18180\n71#7:18188\n68#7,6:18189\n74#7:18223\n78#7:18317\n71#7:18426\n69#7,5:18427\n74#7:18460\n78#7:18470\n71#7:18523\n69#7,5:18524\n74#7:18557\n78#7:18561\n71#7:18569\n68#7,6:18570\n74#7:18604\n78#7:18776\n71#7:18780\n69#7,5:18781\n74#7:18814\n78#7:18860\n71#7:18867\n69#7,5:18868\n74#7:18901\n78#7:18909\n71#7:18949\n69#7,5:18950\n74#7:18983\n78#7:18987\n71#7:18995\n68#7,6:18996\n74#7:19030\n78#7:19202\n71#7:19253\n69#7,5:19254\n74#7:19287\n78#7:19295\n71#7:19306\n68#7,6:19307\n74#7:19341\n71#7:19411\n69#7,5:19412\n74#7:19445\n78#7:19449\n71#7:19452\n69#7,5:19453\n74#7:19486\n78#7:19490\n71#7:19579\n68#7,6:19580\n74#7:19614\n78#7:19618\n71#7:19634\n68#7,6:19635\n74#7:19669\n78#7:19673\n78#7:19735\n71#7:19773\n68#7,6:19774\n74#7:19808\n71#7:19879\n69#7,5:19880\n74#7:19913\n78#7:19917\n71#7:19965\n69#7,5:19966\n74#7:19999\n78#7:20004\n78#7:20012\n71#7:20017\n68#7,6:20018\n74#7:20052\n71#7:20165\n68#7,6:20166\n74#7:20200\n78#7:20204\n71#7:20249\n68#7,6:20250\n74#7:20284\n78#7:20289\n78#7:20297\n71#7:20299\n68#7,6:20300\n74#7:20334\n78#7:20338\n71#7:20339\n69#7,5:20340\n74#7:20373\n78#7:20421\n71#7:20423\n69#7,5:20424\n74#7:20457\n71#7:20461\n69#7,5:20462\n74#7:20495\n78#7:20542\n78#7:20546\n71#7:20556\n68#7,6:20557\n74#7:20591\n71#7:20594\n69#7,5:20595\n74#7:20628\n78#7:20632\n78#7:20636\n71#7:20642\n69#7,5:20643\n74#7:20676\n78#7:20683\n79#8,6:7139\n86#8,4:7154\n90#8,2:7164\n79#8,6:7175\n86#8,4:7190\n90#8,2:7200\n94#8:7234\n94#8:7382\n79#8,6:7700\n86#8,4:7715\n90#8,2:7725\n79#8,6:7736\n86#8,4:7751\n90#8,2:7761\n79#8,6:7773\n86#8,4:7788\n90#8,2:7798\n79#8,6:7809\n86#8,4:7824\n90#8,2:7834\n79#8,6:7855\n86#8,4:7870\n90#8,2:7880\n79#8,6:7892\n86#8,4:7907\n90#8,2:7917\n94#8:7925\n94#8:7929\n94#8:7957\n94#8:7961\n79#8,6:7990\n86#8,4:8005\n90#8,2:8015\n79#8,6:8028\n86#8,4:8043\n90#8,2:8053\n79#8,6:8071\n86#8,4:8086\n90#8,2:8096\n94#8:8109\n79#8,6:8127\n86#8,4:8142\n90#8,2:8152\n79#8,6:8164\n86#8,4:8179\n90#8,2:8189\n94#8:8196\n94#8:8200\n94#8:8204\n79#8,6:8213\n86#8,4:8228\n90#8,2:8238\n94#8:8250\n79#8,6:8260\n86#8,4:8275\n90#8,2:8285\n79#8,6:8304\n86#8,4:8319\n90#8,2:8329\n94#8:8336\n94#8:8359\n94#8:8363\n79#8,6:8389\n86#8,4:8404\n90#8,2:8414\n79#8,6:8421\n86#8,4:8436\n90#8,2:8446\n79#8,6:8458\n86#8,4:8473\n90#8,2:8483\n79#8,6:8496\n86#8,4:8511\n90#8,2:8521\n94#8:8527\n94#8:8531\n79#8,6:8541\n86#8,4:8556\n90#8,2:8566\n94#8:8574\n94#8:8578\n94#8:8582\n79#8,6:8599\n86#8,4:8614\n90#8,2:8624\n79#8,6:8644\n86#8,4:8659\n90#8,2:8669\n79#8,6:8676\n86#8,4:8691\n90#8,2:8701\n79#8,6:8713\n86#8,4:8728\n90#8,2:8738\n94#8:8744\n79#8,6:8754\n86#8,4:8769\n90#8,2:8779\n94#8:8787\n94#8:8791\n94#8:8795\n94#8:8799\n94#8:8812\n94#8:8906\n79#8,6:8932\n86#8,4:8947\n90#8,2:8957\n79#8,6:8969\n86#8,4:8984\n90#8,2:8994\n94#8:9001\n94#8:9005\n79#8,6:9026\n86#8,4:9041\n90#8,2:9051\n79#8,6:9064\n86#8,4:9079\n90#8,2:9089\n94#8:9097\n94#8:9101\n79#8,6:9323\n86#8,4:9338\n90#8,2:9348\n79#8,6:9359\n86#8,4:9374\n90#8,2:9384\n79#8,6:9397\n86#8,4:9412\n90#8,2:9422\n79#8,6:9433\n86#8,4:9448\n90#8,2:9458\n79#8,6:9472\n86#8,4:9487\n90#8,2:9497\n94#8:9504\n79#8,6:9514\n86#8,4:9529\n90#8,2:9539\n94#8:9545\n94#8:9555\n94#8:9559\n79#8,6:9611\n86#8,4:9626\n90#8,2:9636\n79#8,6:9656\n86#8,4:9671\n90#8,2:9681\n79#8,6:9693\n86#8,4:9708\n90#8,2:9718\n94#8:9726\n94#8:9730\n94#8:9734\n94#8:9739\n94#8:9773\n79#8,6:10026\n86#8,4:10041\n90#8,2:10051\n79#8,6:10063\n86#8,4:10078\n90#8,2:10088\n94#8:10095\n79#8,6:10104\n86#8,4:10119\n90#8,2:10129\n79#8,6:10154\n86#8,4:10169\n90#8,2:10179\n94#8:10185\n94#8:10189\n79#8,6:10200\n86#8,4:10215\n90#8,2:10225\n79#8,6:10243\n86#8,4:10258\n90#8,2:10268\n94#8:10274\n79#8,6:10290\n86#8,4:10305\n90#8,2:10315\n94#8:10322\n79#8,6:10338\n86#8,4:10353\n90#8,2:10363\n94#8:10369\n94#8:10373\n94#8:10377\n79#8,6:10392\n86#8,4:10407\n90#8,2:10417\n79#8,6:10438\n86#8,4:10453\n90#8,2:10463\n79#8,6:10475\n86#8,4:10490\n90#8,2:10500\n79#8,6:10514\n86#8,4:10529\n90#8,2:10539\n94#8:10545\n94#8:10549\n94#8:10553\n79#8,6:10572\n86#8,4:10587\n90#8,2:10597\n79#8,6:10609\n86#8,4:10624\n90#8,2:10634\n79#8,6:10648\n86#8,4:10663\n90#8,2:10673\n94#8:10679\n94#8:10683\n94#8:10687\n94#8:10691\n79#8,6:10734\n86#8,4:10749\n90#8,2:10759\n79#8,6:10771\n86#8,4:10786\n90#8,2:10796\n94#8:10803\n94#8:10807\n79#8,6:10835\n86#8,4:10850\n90#8,2:10860\n79#8,6:10881\n86#8,4:10896\n90#8,2:10906\n79#8,6:10917\n86#8,4:10932\n90#8,2:10942\n79#8,6:10956\n86#8,4:10971\n90#8,2:10981\n79#8,6:10993\n86#8,4:11008\n90#8,2:11018\n94#8:11025\n94#8:11030\n79#8,6:11041\n86#8,4:11056\n90#8,2:11066\n94#8:11073\n94#8:11077\n94#8:11081\n79#8,6:11100\n86#8,4:11115\n90#8,2:11125\n79#8,6:11136\n86#8,4:11151\n90#8,2:11161\n79#8,6:11175\n86#8,4:11190\n90#8,2:11200\n94#8:11207\n79#8,6:11218\n86#8,4:11233\n90#8,2:11243\n94#8:11250\n94#8:11254\n94#8:11258\n94#8:11262\n79#8,6:11290\n86#8,4:11305\n90#8,2:11315\n94#8:11322\n79#8,6:11365\n86#8,4:11380\n90#8,2:11390\n79#8,6:11397\n86#8,4:11412\n90#8,2:11422\n79#8,6:11435\n86#8,4:11450\n90#8,2:11460\n79#8,6:11474\n86#8,4:11489\n90#8,2:11499\n94#8:11505\n79#8,6:11517\n86#8,4:11532\n90#8,2:11542\n94#8:11548\n94#8:11552\n79#8,6:11564\n86#8,4:11579\n90#8,2:11589\n94#8:11595\n94#8:11599\n79#8,6:11609\n86#8,4:11624\n90#8,2:11634\n94#8:11643\n79#8,6:11656\n86#8,4:11671\n90#8,2:11681\n79#8,6:11693\n86#8,4:11708\n90#8,2:11718\n79#8,6:11728\n86#8,4:11743\n90#8,2:11753\n94#8:11759\n94#8:11772\n94#8:11776\n79#8,6:11782\n86#8,4:11797\n90#8,2:11807\n79#8,6:11826\n86#8,4:11841\n90#8,2:11851\n94#8:11860\n79#8,6:11879\n86#8,4:11894\n90#8,2:11904\n94#8:11910\n94#8:11914\n94#8:11922\n79#8,6:11932\n86#8,4:11947\n90#8,2:11957\n79#8,6:11964\n86#8,4:11979\n90#8,2:11989\n79#8,6:12001\n86#8,4:12016\n90#8,2:12026\n79#8,6:12040\n86#8,4:12055\n90#8,2:12065\n79#8,6:12077\n86#8,4:12092\n90#8,2:12102\n94#8:12109\n94#8:12113\n79#8,6:12126\n86#8,4:12141\n90#8,2:12151\n94#8:12157\n79#8,6:12170\n86#8,4:12185\n90#8,2:12195\n94#8:12201\n94#8:12205\n79#8,6:12215\n86#8,4:12230\n90#8,2:12240\n94#8:12248\n94#8:12252\n79#8,6:12262\n86#8,4:12277\n90#8,2:12287\n79#8,6:12299\n86#8,4:12314\n90#8,2:12324\n94#8:12332\n79#8,6:12343\n86#8,4:12358\n90#8,2:12368\n94#8:12376\n94#8:12380\n79#8,6:12393\n86#8,4:12408\n90#8,2:12418\n79#8,6:12430\n86#8,4:12445\n90#8,2:12455\n94#8:12462\n94#8:12466\n79#8,6:12480\n86#8,4:12495\n90#8,2:12505\n79#8,6:12516\n86#8,4:12531\n90#8,2:12541\n79#8,6:12548\n86#8,4:12563\n90#8,2:12573\n79#8,6:12585\n86#8,4:12600\n90#8,2:12610\n94#8:12616\n79#8,6:12626\n86#8,4:12641\n90#8,2:12651\n94#8:12657\n94#8:12661\n79#8,6:12673\n86#8,4:12688\n90#8,2:12698\n94#8:12705\n79#8,6:12711\n86#8,4:12726\n90#8,2:12736\n94#8:12742\n94#8:12746\n94#8:12750\n79#8,6:12769\n86#8,4:12784\n90#8,2:12794\n79#8,6:12801\n86#8,4:12816\n90#8,2:12826\n79#8,6:12838\n86#8,4:12853\n90#8,2:12863\n94#8:12870\n94#8:12874\n94#8:12878\n79#8,6:12891\n86#8,4:12906\n90#8,2:12916\n94#8:12922\n79#8,6:12929\n86#8,4:12944\n90#8,2:12954\n79#8,6:12973\n86#8,4:12988\n90#8,2:12998\n79#8,6:13010\n86#8,4:13025\n90#8,2:13035\n94#8:13043\n94#8:13047\n79#8,6:13063\n86#8,4:13078\n90#8,2:13088\n94#8:13095\n79#8,6:13111\n86#8,4:13126\n90#8,2:13136\n94#8:13143\n79#8,6:13159\n86#8,4:13174\n90#8,2:13184\n94#8:13191\n94#8:13195\n94#8:13199\n79#8,6:13295\n86#8,4:13310\n90#8,2:13320\n94#8:13326\n79#8,6:13335\n86#8,4:13350\n90#8,2:13360\n94#8:13367\n79#8,6:13377\n86#8,4:13392\n90#8,2:13402\n79#8,6:13424\n86#8,4:13439\n90#8,2:13449\n79#8,6:13461\n86#8,4:13476\n90#8,2:13486\n94#8:13494\n94#8:13498\n79#8,6:13504\n86#8,4:13519\n90#8,2:13529\n79#8,6:13543\n86#8,4:13558\n90#8,2:13568\n94#8:13574\n79#8,6:13592\n86#8,4:13607\n90#8,2:13617\n94#8:13626\n94#8:13630\n79#8,6:13636\n86#8,4:13651\n90#8,2:13661\n79#8,6:13675\n86#8,4:13690\n90#8,2:13700\n79#8,6:13712\n86#8,4:13727\n90#8,2:13737\n79#8,6:13751\n86#8,4:13766\n90#8,2:13776\n94#8:13782\n94#8:13786\n94#8:13790\n79#8,6:13800\n86#8,4:13815\n90#8,2:13825\n94#8:13844\n94#8:13848\n94#8:13852\n79#8,6:13863\n86#8,4:13878\n90#8,2:13888\n79#8,6:13895\n86#8,4:13910\n90#8,2:13920\n79#8,6:13932\n86#8,4:13947\n90#8,2:13957\n94#8:13965\n79#8,6:13977\n86#8,4:13992\n90#8,2:14002\n94#8:14008\n94#8:14012\n79#8,6:14019\n86#8,4:14034\n90#8,2:14044\n79#8,6:14061\n86#8,4:14076\n90#8,2:14086\n94#8:14092\n79#8,6:14107\n86#8,4:14122\n90#8,2:14132\n94#8:14138\n79#8,6:14153\n86#8,4:14168\n90#8,2:14178\n94#8:14184\n79#8,6:14199\n86#8,4:14214\n90#8,2:14224\n94#8:14230\n79#8,6:14245\n86#8,4:14260\n90#8,2:14270\n94#8:14276\n79#8,6:14292\n86#8,4:14307\n90#8,2:14317\n94#8:14323\n94#8:14327\n79#8,6:14344\n86#8,4:14359\n90#8,2:14369\n79#8,6:14381\n86#8,4:14396\n90#8,2:14406\n94#8:14414\n94#8:14418\n94#8:14422\n79#8,6:14432\n86#8,4:14447\n90#8,2:14457\n79#8,6:14468\n86#8,4:14483\n90#8,2:14493\n94#8:14499\n79#8,6:14513\n86#8,4:14528\n90#8,2:14538\n79#8,6:14549\n86#8,4:14564\n90#8,2:14574\n79#8,6:14581\n86#8,4:14596\n90#8,2:14606\n79#8,6:14619\n86#8,4:14634\n90#8,2:14644\n94#8:14650\n79#8,6:14661\n86#8,4:14676\n90#8,2:14686\n94#8:14692\n94#8:14696\n79#8,6:14706\n86#8,4:14721\n90#8,2:14731\n94#8:14737\n94#8:14742\n94#8:14746\n79#8,6:14760\n86#8,4:14775\n90#8,2:14785\n79#8,6:14792\n86#8,4:14807\n90#8,2:14817\n79#8,6:14828\n86#8,4:14843\n90#8,2:14853\n94#8:14859\n94#8:14871\n94#8:14875\n79#8,6:14889\n86#8,4:14904\n90#8,2:14914\n79#8,6:14925\n86#8,4:14940\n90#8,2:14950\n79#8,6:14957\n86#8,4:14972\n90#8,2:14982\n79#8,6:14993\n86#8,4:15008\n90#8,2:15018\n94#8:15024\n94#8:15036\n94#8:15041\n94#8:15045\n79#8,6:15056\n86#8,4:15071\n90#8,2:15081\n94#8:15105\n79#8,6:15115\n86#8,4:15130\n90#8,2:15140\n94#8:15164\n79#8,6:15176\n86#8,4:15191\n90#8,2:15201\n79#8,6:15213\n86#8,4:15228\n90#8,2:15238\n94#8:15245\n94#8:15249\n94#8:15256\n79#8,6:15304\n86#8,4:15319\n90#8,2:15329\n94#8:15336\n79#8,6:15345\n86#8,4:15360\n90#8,2:15370\n79#8,6:15398\n86#8,4:15413\n90#8,2:15423\n79#8,6:15435\n86#8,4:15450\n90#8,2:15460\n94#8:15467\n94#8:15471\n79#8,6:15490\n86#8,4:15505\n90#8,2:15515\n79#8,6:15528\n86#8,4:15543\n90#8,2:15553\n79#8,6:15560\n86#8,4:15575\n90#8,2:15585\n79#8,6:15598\n86#8,4:15613\n90#8,2:15623\n94#8:15629\n94#8:15633\n94#8:15637\n94#8:15641\n79#8,6:15657\n86#8,4:15672\n90#8,2:15682\n94#8:15695\n79#8,6:15715\n86#8,4:15730\n90#8,2:15740\n79#8,6:15752\n86#8,4:15767\n90#8,2:15777\n79#8,6:15789\n86#8,4:15804\n90#8,2:15814\n94#8:15827\n94#8:15833\n94#8:15837\n94#8:15841\n79#8,6:15859\n86#8,4:15874\n90#8,2:15884\n79#8,6:15897\n86#8,4:15912\n90#8,2:15922\n94#8:15938\n94#8:15942\n79#8,6:15961\n86#8,4:15976\n90#8,2:15986\n94#8:15999\n79#8,6:16039\n86#8,4:16054\n90#8,2:16064\n79#8,6:16071\n86#8,4:16086\n90#8,2:16096\n94#8:16102\n94#8:16135\n79#8,6:16193\n86#8,4:16208\n90#8,2:16218\n94#8:16225\n79#8,6:16247\n86#8,4:16262\n90#8,2:16272\n79#8,6:16283\n86#8,4:16298\n90#8,2:16308\n79#8,6:16322\n86#8,4:16337\n90#8,2:16347\n94#8:16353\n79#8,6:16364\n86#8,4:16379\n90#8,2:16389\n94#8:16395\n94#8:16399\n79#8,6:16419\n86#8,4:16434\n90#8,2:16444\n79#8,6:16451\n86#8,4:16466\n90#8,2:16476\n79#8,6:16488\n86#8,4:16503\n90#8,2:16513\n79#8,6:16525\n86#8,4:16540\n90#8,2:16550\n94#8:16556\n79#8,6:16567\n86#8,4:16582\n90#8,2:16592\n94#8:16598\n94#8:16602\n94#8:16606\n94#8:16610\n79#8,6:16631\n86#8,4:16646\n90#8,2:16656\n79#8,6:16668\n86#8,4:16683\n90#8,2:16693\n79#8,6:16704\n86#8,4:16719\n90#8,2:16729\n94#8:16735\n94#8:16741\n79#8,6:16751\n86#8,4:16766\n90#8,2:16776\n79#8,6:16792\n86#8,4:16807\n90#8,2:16817\n94#8:16823\n94#8:16827\n94#8:16831\n94#8:16835\n79#8,6:16851\n86#8,4:16866\n90#8,2:16876\n79#8,6:16887\n86#8,4:16902\n90#8,2:16912\n79#8,6:16926\n86#8,4:16941\n90#8,2:16951\n94#8:16957\n79#8,6:16968\n86#8,4:16983\n90#8,2:16993\n94#8:16999\n94#8:17003\n79#8,6:17022\n86#8,4:17037\n90#8,2:17047\n79#8,6:17068\n86#8,4:17083\n90#8,2:17093\n94#8:17099\n94#8:17104\n79#8,6:17130\n86#8,4:17145\n90#8,2:17155\n79#8,6:17175\n86#8,4:17190\n90#8,2:17200\n94#8:17206\n79#8,6:17224\n86#8,4:17239\n90#8,2:17249\n94#8:17255\n94#8:17259\n94#8:17263\n79#8,6:17300\n86#8,4:17315\n90#8,2:17325\n79#8,6:17332\n86#8,4:17347\n90#8,2:17357\n79#8,6:17368\n86#8,4:17383\n90#8,2:17393\n79#8,6:17406\n86#8,4:17421\n90#8,2:17431\n94#8:17438\n94#8:17442\n79#8,6:17452\n86#8,4:17467\n90#8,2:17477\n94#8:17484\n94#8:17488\n79#8,6:17497\n86#8,4:17512\n90#8,2:17522\n79#8,6:17533\n86#8,4:17548\n90#8,2:17558\n79#8,6:17570\n86#8,4:17585\n90#8,2:17595\n94#8:17601\n79#8,6:17614\n86#8,4:17629\n90#8,2:17639\n94#8:17651\n94#8:17655\n79#8,6:17664\n86#8,4:17679\n90#8,2:17689\n79#8,6:17704\n86#8,4:17719\n90#8,2:17729\n94#8:17741\n94#8:17745\n94#8:17749\n79#8,6:17758\n86#8,4:17773\n90#8,2:17783\n94#8:17790\n79#8,6:17799\n86#8,4:17814\n90#8,2:17824\n79#8,6:17845\n86#8,4:17860\n90#8,2:17870\n79#8,6:17877\n86#8,4:17892\n90#8,2:17902\n94#8:17909\n94#8:17913\n94#8:17917\n79#8,6:17926\n86#8,4:17941\n90#8,2:17951\n79#8,6:17966\n86#8,4:17981\n90#8,2:17991\n79#8,6:17998\n86#8,4:18013\n90#8,2:18023\n94#8:18042\n94#8:18046\n94#8:18050\n79#8,6:18059\n86#8,4:18074\n90#8,2:18084\n79#8,6:18099\n86#8,4:18114\n90#8,2:18124\n79#8,6:18131\n86#8,4:18146\n90#8,2:18156\n94#8:18175\n94#8:18179\n94#8:18183\n79#8,6:18195\n86#8,4:18210\n90#8,2:18220\n79#8,6:18231\n86#8,4:18246\n90#8,2:18256\n79#8,6:18276\n86#8,4:18291\n90#8,2:18301\n94#8:18308\n94#8:18312\n94#8:18316\n94#8:18328\n79#8,6:18356\n86#8,4:18371\n90#8,2:18381\n79#8,6:18388\n86#8,4:18403\n90#8,2:18413\n94#8:18420\n79#8,6:18432\n86#8,4:18447\n90#8,2:18457\n94#8:18469\n94#8:18480\n79#8,6:18492\n86#8,4:18507\n90#8,2:18517\n79#8,6:18529\n86#8,4:18544\n90#8,2:18554\n94#8:18560\n79#8,6:18576\n86#8,4:18591\n90#8,2:18601\n79#8,6:18614\n86#8,4:18629\n90#8,2:18639\n79#8,6:18651\n86#8,4:18666\n90#8,2:18676\n94#8:18683\n79#8,6:18693\n86#8,4:18708\n90#8,2:18718\n94#8:18725\n79#8,6:18735\n86#8,4:18750\n90#8,2:18760\n94#8:18767\n94#8:18771\n94#8:18775\n79#8,6:18786\n86#8,4:18801\n90#8,2:18811\n79#8,6:18823\n86#8,4:18838\n90#8,2:18848\n94#8:18855\n94#8:18859\n94#8:18864\n79#8,6:18873\n86#8,4:18888\n90#8,2:18898\n94#8:18908\n79#8,6:18918\n86#8,4:18933\n90#8,2:18943\n79#8,6:18955\n86#8,4:18970\n90#8,2:18980\n94#8:18986\n79#8,6:19002\n86#8,4:19017\n90#8,2:19027\n79#8,6:19040\n86#8,4:19055\n90#8,2:19065\n79#8,6:19077\n86#8,4:19092\n90#8,2:19102\n94#8:19109\n79#8,6:19119\n86#8,4:19134\n90#8,2:19144\n94#8:19151\n79#8,6:19161\n86#8,4:19176\n90#8,2:19186\n94#8:19193\n94#8:19197\n94#8:19201\n79#8,6:19211\n86#8,4:19226\n90#8,2:19236\n94#8:19246\n94#8:19250\n79#8,6:19259\n86#8,4:19274\n90#8,2:19284\n94#8:19294\n79#8,6:19313\n86#8,4:19328\n90#8,2:19338\n79#8,6:19345\n86#8,4:19360\n90#8,2:19370\n79#8,6:19381\n86#8,4:19396\n90#8,2:19406\n79#8,6:19417\n86#8,4:19432\n90#8,2:19442\n94#8:19448\n79#8,6:19458\n86#8,4:19473\n90#8,2:19483\n94#8:19489\n79#8,6:19500\n86#8,4:19515\n90#8,2:19525\n79#8,6:19537\n86#8,4:19552\n90#8,2:19562\n79#8,6:19586\n86#8,4:19601\n90#8,2:19611\n94#8:19617\n79#8,6:19641\n86#8,4:19656\n90#8,2:19666\n94#8:19672\n94#8:19676\n94#8:19681\n94#8:19685\n79#8,6:19695\n86#8,4:19710\n90#8,2:19720\n94#8:19726\n94#8:19730\n94#8:19734\n79#8,6:19743\n86#8,4:19758\n90#8,2:19768\n79#8,6:19780\n86#8,4:19795\n90#8,2:19805\n79#8,6:19812\n86#8,4:19827\n90#8,2:19837\n79#8,6:19848\n86#8,4:19863\n90#8,2:19873\n79#8,6:19885\n86#8,4:19900\n90#8,2:19910\n94#8:19916\n79#8,6:19927\n86#8,4:19942\n90#8,2:19952\n94#8:19958\n94#8:19962\n79#8,6:19971\n86#8,4:19986\n90#8,2:19996\n94#8:20003\n94#8:20007\n94#8:20011\n79#8,6:20024\n86#8,4:20039\n90#8,2:20049\n79#8,6:20060\n86#8,4:20075\n90#8,2:20085\n79#8,6:20092\n86#8,4:20107\n90#8,2:20117\n79#8,6:20128\n86#8,4:20143\n90#8,2:20153\n94#8:20160\n79#8,6:20172\n86#8,4:20187\n90#8,2:20197\n94#8:20203\n94#8:20207\n79#8,6:20213\n86#8,4:20228\n90#8,2:20238\n94#8:20244\n79#8,6:20256\n86#8,4:20271\n90#8,2:20281\n94#8:20288\n94#8:20292\n94#8:20296\n79#8,6:20306\n86#8,4:20321\n90#8,2:20331\n94#8:20337\n79#8,6:20345\n86#8,4:20360\n90#8,2:20370\n79#8,6:20382\n86#8,4:20397\n90#8,2:20407\n94#8:20416\n94#8:20420\n79#8,6:20429\n86#8,4:20444\n90#8,2:20454\n79#8,6:20467\n86#8,4:20482\n90#8,2:20492\n79#8,6:20504\n86#8,4:20519\n90#8,2:20529\n94#8:20537\n94#8:20541\n94#8:20545\n79#8,6:20563\n86#8,4:20578\n90#8,2:20588\n79#8,6:20600\n86#8,4:20615\n90#8,2:20625\n94#8:20631\n94#8:20635\n94#8:20639\n79#8,6:20648\n86#8,4:20663\n90#8,2:20673\n94#8:20682\n79#8,6:20692\n86#8,4:20707\n90#8,2:20717\n79#8,6:20724\n86#8,4:20739\n90#8,2:20749\n94#8:20756\n94#8:20765\n368#9,9:7145\n377#9:7166\n368#9,9:7181\n377#9:7202\n378#9,2:7232\n378#9,2:7380\n368#9,9:7706\n377#9:7727\n368#9,9:7742\n377#9:7763\n368#9,9:7779\n377#9:7800\n368#9,9:7815\n377#9:7836\n368#9,9:7861\n377#9:7882\n368#9,9:7898\n377#9:7919\n378#9,2:7923\n378#9,2:7927\n378#9,2:7955\n378#9,2:7959\n368#9,9:7996\n377#9:8017\n368#9,9:8034\n377#9:8055\n368#9,9:8077\n377#9:8098\n378#9,2:8107\n368#9,9:8133\n377#9:8154\n368#9,9:8170\n377#9:8191\n378#9,2:8194\n378#9,2:8198\n378#9,2:8202\n368#9,9:8219\n377#9:8240\n378#9,2:8248\n368#9,9:8266\n377#9:8287\n368#9,9:8310\n377#9:8331\n378#9,2:8334\n378#9,2:8357\n378#9,2:8361\n368#9,9:8395\n377#9:8416\n368#9,9:8427\n377#9:8448\n368#9,9:8464\n377#9:8485\n368#9,9:8502\n377#9:8523\n378#9,2:8525\n378#9,2:8529\n368#9,9:8547\n377#9:8568\n378#9,2:8572\n378#9,2:8576\n378#9,2:8580\n368#9,9:8605\n377#9:8626\n368#9,9:8650\n377#9:8671\n368#9,9:8682\n377#9:8703\n368#9,9:8719\n377#9:8740\n378#9,2:8742\n368#9,9:8760\n377#9:8781\n378#9,2:8785\n378#9,2:8789\n378#9,2:8793\n378#9,2:8797\n378#9,2:8810\n378#9,2:8904\n368#9,9:8938\n377#9:8959\n368#9,9:8975\n377#9:8996\n378#9,2:8999\n378#9,2:9003\n368#9,9:9032\n377#9:9053\n368#9,9:9070\n377#9:9091\n378#9,2:9095\n378#9,2:9099\n368#9,9:9329\n377#9:9350\n368#9,9:9365\n377#9:9386\n368#9,9:9403\n377#9:9424\n368#9,9:9439\n377#9:9460\n368#9,9:9478\n377#9:9499\n378#9,2:9502\n368#9,9:9520\n377#9:9541\n378#9,2:9543\n378#9,2:9553\n378#9,2:9557\n368#9,9:9617\n377#9:9638\n368#9,9:9662\n377#9:9683\n368#9,9:9699\n377#9:9720\n378#9,2:9724\n378#9,2:9728\n378#9,2:9732\n378#9,2:9737\n378#9,2:9771\n368#9,9:10032\n377#9:10053\n368#9,9:10069\n377#9:10090\n378#9,2:10093\n368#9,9:10110\n377#9:10131\n368#9,9:10160\n377#9:10181\n378#9,2:10183\n378#9,2:10187\n368#9,9:10206\n377#9:10227\n368#9,9:10249\n377#9:10270\n378#9,2:10272\n368#9,9:10296\n377#9:10317\n378#9,2:10320\n368#9,9:10344\n377#9:10365\n378#9,2:10367\n378#9,2:10371\n378#9,2:10375\n368#9,9:10398\n377#9:10419\n368#9,9:10444\n377#9:10465\n368#9,9:10481\n377#9:10502\n368#9,9:10520\n377#9:10541\n378#9,2:10543\n378#9,2:10547\n378#9,2:10551\n368#9,9:10578\n377#9:10599\n368#9,9:10615\n377#9:10636\n368#9,9:10654\n377#9:10675\n378#9,2:10677\n378#9,2:10681\n378#9,2:10685\n378#9,2:10689\n368#9,9:10740\n377#9:10761\n368#9,9:10777\n377#9:10798\n378#9,2:10801\n378#9,2:10805\n368#9,9:10841\n377#9:10862\n368#9,9:10887\n377#9:10908\n368#9,9:10923\n377#9:10944\n368#9,9:10962\n377#9:10983\n368#9,9:10999\n377#9:11020\n378#9,2:11023\n378#9,2:11028\n368#9,9:11047\n377#9:11068\n378#9,2:11071\n378#9,2:11075\n378#9,2:11079\n368#9,9:11106\n377#9:11127\n368#9,9:11142\n377#9:11163\n368#9,9:11181\n377#9:11202\n378#9,2:11205\n368#9,9:11224\n377#9:11245\n378#9,2:11248\n378#9,2:11252\n378#9,2:11256\n378#9,2:11260\n368#9,9:11296\n377#9:11317\n378#9,2:11320\n368#9,9:11371\n377#9:11392\n368#9,9:11403\n377#9:11424\n368#9,9:11441\n377#9:11462\n368#9,9:11480\n377#9:11501\n378#9,2:11503\n368#9,9:11523\n377#9:11544\n378#9,2:11546\n378#9,2:11550\n368#9,9:11570\n377#9:11591\n378#9,2:11593\n378#9,2:11597\n368#9,9:11615\n377#9:11636\n378#9,2:11641\n368#9,9:11662\n377#9:11683\n368#9,9:11699\n377#9:11720\n368#9,9:11734\n377#9:11755\n378#9,2:11757\n378#9,2:11770\n378#9,2:11774\n368#9,9:11788\n377#9:11809\n368#9,9:11832\n377#9:11853\n378#9,2:11858\n368#9,9:11885\n377#9:11906\n378#9,2:11908\n378#9,2:11912\n378#9,2:11920\n368#9,9:11938\n377#9:11959\n368#9,9:11970\n377#9:11991\n368#9,9:12007\n377#9:12028\n368#9,9:12046\n377#9:12067\n368#9,9:12083\n377#9:12104\n378#9,2:12107\n378#9,2:12111\n368#9,9:12132\n377#9:12153\n378#9,2:12155\n368#9,9:12176\n377#9:12197\n378#9,2:12199\n378#9,2:12203\n368#9,9:12221\n377#9:12242\n378#9,2:12246\n378#9,2:12250\n368#9,9:12268\n377#9:12289\n368#9,9:12305\n377#9:12326\n378#9,2:12330\n368#9,9:12349\n377#9:12370\n378#9,2:12374\n378#9,2:12378\n368#9,9:12399\n377#9:12420\n368#9,9:12436\n377#9:12457\n378#9,2:12460\n378#9,2:12464\n368#9,9:12486\n377#9:12507\n368#9,9:12522\n377#9:12543\n368#9,9:12554\n377#9:12575\n368#9,9:12591\n377#9:12612\n378#9,2:12614\n368#9,9:12632\n377#9:12653\n378#9,2:12655\n378#9,2:12659\n368#9,9:12679\n377#9:12700\n378#9,2:12703\n368#9,9:12717\n377#9:12738\n378#9,2:12740\n378#9,2:12744\n378#9,2:12748\n368#9,9:12775\n377#9:12796\n368#9,9:12807\n377#9:12828\n368#9,9:12844\n377#9:12865\n378#9,2:12868\n378#9,2:12872\n378#9,2:12876\n368#9,9:12897\n377#9:12918\n378#9,2:12920\n368#9,9:12935\n377#9:12956\n368#9,9:12979\n377#9:13000\n368#9,9:13016\n377#9:13037\n378#9,2:13041\n378#9,2:13045\n368#9,9:13069\n377#9:13090\n378#9,2:13093\n368#9,9:13117\n377#9:13138\n378#9,2:13141\n368#9,9:13165\n377#9:13186\n378#9,2:13189\n378#9,2:13193\n378#9,2:13197\n368#9,9:13301\n377#9:13322\n378#9,2:13324\n368#9,9:13341\n377#9:13362\n378#9,2:13365\n368#9,9:13383\n377#9:13404\n368#9,9:13430\n377#9:13451\n368#9,9:13467\n377#9:13488\n378#9,2:13492\n378#9,2:13496\n368#9,9:13510\n377#9:13531\n368#9,9:13549\n377#9:13570\n378#9,2:13572\n368#9,9:13598\n377#9:13619\n378#9,2:13624\n378#9,2:13628\n368#9,9:13642\n377#9:13663\n368#9,9:13681\n377#9:13702\n368#9,9:13718\n377#9:13739\n368#9,9:13757\n377#9:13778\n378#9,2:13780\n378#9,2:13784\n378#9,2:13788\n368#9,9:13806\n377#9:13827\n378#9,2:13842\n378#9,2:13846\n378#9,2:13850\n368#9,9:13869\n377#9:13890\n368#9,9:13901\n377#9:13922\n368#9,9:13938\n377#9:13959\n378#9,2:13963\n368#9,9:13983\n377#9:14004\n378#9,2:14006\n378#9,2:14010\n368#9,9:14025\n377#9:14046\n368#9,9:14067\n377#9:14088\n378#9,2:14090\n368#9,9:14113\n377#9:14134\n378#9,2:14136\n368#9,9:14159\n377#9:14180\n378#9,2:14182\n368#9,9:14205\n377#9:14226\n378#9,2:14228\n368#9,9:14251\n377#9:14272\n378#9,2:14274\n368#9,9:14298\n377#9:14319\n378#9,2:14321\n378#9,2:14325\n368#9,9:14350\n377#9:14371\n368#9,9:14387\n377#9:14408\n378#9,2:14412\n378#9,2:14416\n378#9,2:14420\n368#9,9:14438\n377#9:14459\n368#9,9:14474\n377#9:14495\n378#9,2:14497\n368#9,9:14519\n377#9:14540\n368#9,9:14555\n377#9:14576\n368#9,9:14587\n377#9:14608\n368#9,9:14625\n377#9:14646\n378#9,2:14648\n368#9,9:14667\n377#9:14688\n378#9,2:14690\n378#9,2:14694\n368#9,9:14712\n377#9:14733\n378#9,2:14735\n378#9,2:14740\n378#9,2:14744\n368#9,9:14766\n377#9:14787\n368#9,9:14798\n377#9:14819\n368#9,9:14834\n377#9:14855\n378#9,2:14857\n378#9,2:14869\n378#9,2:14873\n368#9,9:14895\n377#9:14916\n368#9,9:14931\n377#9:14952\n368#9,9:14963\n377#9:14984\n368#9,9:14999\n377#9:15020\n378#9,2:15022\n378#9,2:15034\n378#9,2:15039\n378#9,2:15043\n368#9,9:15062\n377#9:15083\n378#9,2:15103\n368#9,9:15121\n377#9:15142\n378#9,2:15162\n368#9,9:15182\n377#9:15203\n368#9,9:15219\n377#9:15240\n378#9,2:15243\n378#9,2:15247\n378#9,2:15254\n368#9,9:15310\n377#9:15331\n378#9,2:15334\n368#9,9:15351\n377#9:15372\n368#9,9:15404\n377#9:15425\n368#9,9:15441\n377#9:15462\n378#9,2:15465\n378#9,2:15469\n368#9,9:15496\n377#9:15517\n368#9,9:15534\n377#9:15555\n368#9,9:15566\n377#9:15587\n368#9,9:15604\n377#9:15625\n378#9,2:15627\n378#9,2:15631\n378#9,2:15635\n378#9,2:15639\n368#9,9:15663\n377#9:15684\n378#9,2:15693\n368#9,9:15721\n377#9:15742\n368#9,9:15758\n377#9:15779\n368#9,9:15795\n377#9:15816\n378#9,2:15825\n378#9,2:15831\n378#9,2:15835\n378#9,2:15839\n368#9,9:15865\n377#9:15886\n368#9,9:15903\n377#9:15924\n378#9,2:15936\n378#9,2:15940\n368#9,9:15967\n377#9:15988\n378#9,2:15997\n368#9,9:16045\n377#9:16066\n368#9,9:16077\n377#9:16098\n378#9,2:16100\n378#9,2:16133\n368#9,9:16199\n377#9:16220\n378#9,2:16223\n368#9,9:16253\n377#9:16274\n368#9,9:16289\n377#9:16310\n368#9,9:16328\n377#9:16349\n378#9,2:16351\n368#9,9:16370\n377#9:16391\n378#9,2:16393\n378#9,2:16397\n368#9,9:16425\n377#9:16446\n368#9,9:16457\n377#9:16478\n368#9,9:16494\n377#9:16515\n368#9,9:16531\n377#9:16552\n378#9,2:16554\n368#9,9:16573\n377#9:16594\n378#9,2:16596\n378#9,2:16600\n378#9,2:16604\n378#9,2:16608\n368#9,9:16637\n377#9:16658\n368#9,9:16674\n377#9:16695\n368#9,9:16710\n377#9:16731\n378#9,2:16733\n378#9,2:16739\n368#9,9:16757\n377#9:16778\n368#9,9:16798\n377#9:16819\n378#9,2:16821\n378#9,2:16825\n378#9,2:16829\n378#9,2:16833\n368#9,9:16857\n377#9:16878\n368#9,9:16893\n377#9:16914\n368#9,9:16932\n377#9:16953\n378#9,2:16955\n368#9,9:16974\n377#9:16995\n378#9,2:16997\n378#9,2:17001\n368#9,9:17028\n377#9:17049\n368#9,9:17074\n377#9:17095\n378#9,2:17097\n378#9,2:17102\n368#9,9:17136\n377#9:17157\n368#9,9:17181\n377#9:17202\n378#9,2:17204\n368#9,9:17230\n377#9:17251\n378#9,2:17253\n378#9,2:17257\n378#9,2:17261\n368#9,9:17306\n377#9:17327\n368#9,9:17338\n377#9:17359\n368#9,9:17374\n377#9:17395\n368#9,9:17412\n377#9:17433\n378#9,2:17436\n378#9,2:17440\n368#9,9:17458\n377#9:17479\n378#9,2:17482\n378#9,2:17486\n368#9,9:17503\n377#9:17524\n368#9,9:17539\n377#9:17560\n368#9,9:17576\n377#9:17597\n378#9,2:17599\n368#9,9:17620\n377#9:17641\n378#9,2:17649\n378#9,2:17653\n368#9,9:17670\n377#9:17691\n368#9,9:17710\n377#9:17731\n378#9,2:17739\n378#9,2:17743\n378#9,2:17747\n368#9,9:17764\n377#9:17785\n378#9,2:17788\n368#9,9:17805\n377#9:17826\n368#9,9:17851\n377#9:17872\n368#9,9:17883\n377#9:17904\n378#9,2:17907\n378#9,2:17911\n378#9,2:17915\n368#9,9:17932\n377#9:17953\n368#9,9:17972\n377#9:17993\n368#9,9:18004\n377#9:18025\n378#9,2:18040\n378#9,2:18044\n378#9,2:18048\n368#9,9:18065\n377#9:18086\n368#9,9:18105\n377#9:18126\n368#9,9:18137\n377#9:18158\n378#9,2:18173\n378#9,2:18177\n378#9,2:18181\n368#9,9:18201\n377#9:18222\n368#9,9:18237\n377#9:18258\n368#9,9:18282\n377#9:18303\n378#9,2:18306\n378#9,2:18310\n378#9,2:18314\n378#9,2:18326\n368#9,9:18362\n377#9:18383\n368#9,9:18394\n377#9:18415\n378#9,2:18418\n368#9,9:18438\n377#9:18459\n378#9,2:18467\n378#9,2:18478\n368#9,9:18498\n377#9:18519\n368#9,9:18535\n377#9:18556\n378#9,2:18558\n368#9,9:18582\n377#9:18603\n368#9,9:18620\n377#9:18641\n368#9,9:18657\n377#9:18678\n378#9,2:18681\n368#9,9:18699\n377#9:18720\n378#9,2:18723\n368#9,9:18741\n377#9:18762\n378#9,2:18765\n378#9,2:18769\n378#9,2:18773\n368#9,9:18792\n377#9:18813\n368#9,9:18829\n377#9:18850\n378#9,2:18853\n378#9,2:18857\n378#9,2:18862\n368#9,9:18879\n377#9:18900\n378#9,2:18906\n368#9,9:18924\n377#9:18945\n368#9,9:18961\n377#9:18982\n378#9,2:18984\n368#9,9:19008\n377#9:19029\n368#9,9:19046\n377#9:19067\n368#9,9:19083\n377#9:19104\n378#9,2:19107\n368#9,9:19125\n377#9:19146\n378#9,2:19149\n368#9,9:19167\n377#9:19188\n378#9,2:19191\n378#9,2:19195\n378#9,2:19199\n368#9,9:19217\n377#9:19238\n378#9,2:19244\n378#9,2:19248\n368#9,9:19265\n377#9:19286\n378#9,2:19292\n368#9,9:19319\n377#9:19340\n368#9,9:19351\n377#9:19372\n368#9,9:19387\n377#9:19408\n368#9,9:19423\n377#9:19444\n378#9,2:19446\n368#9,9:19464\n377#9:19485\n378#9,2:19487\n368#9,9:19506\n377#9:19527\n368#9,9:19543\n377#9:19564\n368#9,9:19592\n377#9:19613\n378#9,2:19615\n368#9,9:19647\n377#9:19668\n378#9,2:19670\n378#9,2:19674\n378#9,2:19679\n378#9,2:19683\n368#9,9:19701\n377#9:19722\n378#9,2:19724\n378#9,2:19728\n378#9,2:19732\n368#9,9:19749\n377#9:19770\n368#9,9:19786\n377#9:19807\n368#9,9:19818\n377#9:19839\n368#9,9:19854\n377#9:19875\n368#9,9:19891\n377#9:19912\n378#9,2:19914\n368#9,9:19933\n377#9:19954\n378#9,2:19956\n378#9,2:19960\n368#9,9:19977\n377#9:19998\n378#9,2:20001\n378#9,2:20005\n378#9,2:20009\n368#9,9:20030\n377#9:20051\n368#9,9:20066\n377#9:20087\n368#9,9:20098\n377#9:20119\n368#9,9:20134\n377#9:20155\n378#9,2:20158\n368#9,9:20178\n377#9:20199\n378#9,2:20201\n378#9,2:20205\n368#9,9:20219\n377#9:20240\n378#9,2:20242\n368#9,9:20262\n377#9:20283\n378#9,2:20286\n378#9,2:20290\n378#9,2:20294\n368#9,9:20312\n377#9:20333\n378#9,2:20335\n368#9,9:20351\n377#9:20372\n368#9,9:20388\n377#9:20409\n378#9,2:20414\n378#9,2:20418\n368#9,9:20435\n377#9:20456\n368#9,9:20473\n377#9:20494\n368#9,9:20510\n377#9:20531\n378#9,2:20535\n378#9,2:20539\n378#9,2:20543\n368#9,9:20569\n377#9:20590\n368#9,9:20606\n377#9:20627\n378#9,2:20629\n378#9,2:20633\n378#9,2:20637\n368#9,9:20654\n377#9:20675\n378#9,2:20680\n368#9,9:20698\n377#9:20719\n368#9,9:20730\n377#9:20751\n378#9,2:20754\n378#9,2:20763\n4034#10,6:7158\n4034#10,6:7194\n4034#10,6:7719\n4034#10,6:7755\n4034#10,6:7792\n4034#10,6:7828\n4034#10,6:7874\n4034#10,6:7911\n4034#10,6:8009\n4034#10,6:8047\n4034#10,6:8090\n4034#10,6:8146\n4034#10,6:8183\n4034#10,6:8232\n4034#10,6:8279\n4034#10,6:8323\n4034#10,6:8408\n4034#10,6:8440\n4034#10,6:8477\n4034#10,6:8515\n4034#10,6:8560\n4034#10,6:8618\n4034#10,6:8663\n4034#10,6:8695\n4034#10,6:8732\n4034#10,6:8773\n4034#10,6:8951\n4034#10,6:8988\n4034#10,6:9045\n4034#10,6:9083\n4034#10,6:9342\n4034#10,6:9378\n4034#10,6:9416\n4034#10,6:9452\n4034#10,6:9491\n4034#10,6:9533\n4034#10,6:9630\n4034#10,6:9675\n4034#10,6:9712\n4034#10,6:10045\n4034#10,6:10082\n4034#10,6:10123\n4034#10,6:10173\n4034#10,6:10219\n4034#10,6:10262\n4034#10,6:10309\n4034#10,6:10357\n4034#10,6:10411\n4034#10,6:10457\n4034#10,6:10494\n4034#10,6:10533\n4034#10,6:10591\n4034#10,6:10628\n4034#10,6:10667\n4034#10,6:10753\n4034#10,6:10790\n4034#10,6:10854\n4034#10,6:10900\n4034#10,6:10936\n4034#10,6:10975\n4034#10,6:11012\n4034#10,6:11060\n4034#10,6:11119\n4034#10,6:11155\n4034#10,6:11194\n4034#10,6:11237\n4034#10,6:11309\n4034#10,6:11384\n4034#10,6:11416\n4034#10,6:11454\n4034#10,6:11493\n4034#10,6:11536\n4034#10,6:11583\n4034#10,6:11628\n4034#10,6:11675\n4034#10,6:11712\n4034#10,6:11747\n4034#10,6:11801\n4034#10,6:11845\n4034#10,6:11898\n4034#10,6:11951\n4034#10,6:11983\n4034#10,6:12020\n4034#10,6:12059\n4034#10,6:12096\n4034#10,6:12145\n4034#10,6:12189\n4034#10,6:12234\n4034#10,6:12281\n4034#10,6:12318\n4034#10,6:12362\n4034#10,6:12412\n4034#10,6:12449\n4034#10,6:12499\n4034#10,6:12535\n4034#10,6:12567\n4034#10,6:12604\n4034#10,6:12645\n4034#10,6:12692\n4034#10,6:12730\n4034#10,6:12788\n4034#10,6:12820\n4034#10,6:12857\n4034#10,6:12910\n4034#10,6:12948\n4034#10,6:12992\n4034#10,6:13029\n4034#10,6:13082\n4034#10,6:13130\n4034#10,6:13178\n4034#10,6:13314\n4034#10,6:13354\n4034#10,6:13396\n4034#10,6:13443\n4034#10,6:13480\n4034#10,6:13523\n4034#10,6:13562\n4034#10,6:13611\n4034#10,6:13655\n4034#10,6:13694\n4034#10,6:13731\n4034#10,6:13770\n4034#10,6:13819\n4034#10,6:13882\n4034#10,6:13914\n4034#10,6:13951\n4034#10,6:13996\n4034#10,6:14038\n4034#10,6:14080\n4034#10,6:14126\n4034#10,6:14172\n4034#10,6:14218\n4034#10,6:14264\n4034#10,6:14311\n4034#10,6:14363\n4034#10,6:14400\n4034#10,6:14451\n4034#10,6:14487\n4034#10,6:14532\n4034#10,6:14568\n4034#10,6:14600\n4034#10,6:14638\n4034#10,6:14680\n4034#10,6:14725\n4034#10,6:14779\n4034#10,6:14811\n4034#10,6:14847\n4034#10,6:14908\n4034#10,6:14944\n4034#10,6:14976\n4034#10,6:15012\n4034#10,6:15075\n4034#10,6:15134\n4034#10,6:15195\n4034#10,6:15232\n4034#10,6:15323\n4034#10,6:15364\n4034#10,6:15417\n4034#10,6:15454\n4034#10,6:15509\n4034#10,6:15547\n4034#10,6:15579\n4034#10,6:15617\n4034#10,6:15676\n4034#10,6:15734\n4034#10,6:15771\n4034#10,6:15808\n4034#10,6:15878\n4034#10,6:15916\n4034#10,6:15980\n4034#10,6:16058\n4034#10,6:16090\n4034#10,6:16212\n4034#10,6:16266\n4034#10,6:16302\n4034#10,6:16341\n4034#10,6:16383\n4034#10,6:16438\n4034#10,6:16470\n4034#10,6:16507\n4034#10,6:16544\n4034#10,6:16586\n4034#10,6:16650\n4034#10,6:16687\n4034#10,6:16723\n4034#10,6:16770\n4034#10,6:16811\n4034#10,6:16870\n4034#10,6:16906\n4034#10,6:16945\n4034#10,6:16987\n4034#10,6:17041\n4034#10,6:17087\n4034#10,6:17149\n4034#10,6:17194\n4034#10,6:17243\n4034#10,6:17319\n4034#10,6:17351\n4034#10,6:17387\n4034#10,6:17425\n4034#10,6:17471\n4034#10,6:17516\n4034#10,6:17552\n4034#10,6:17589\n4034#10,6:17633\n4034#10,6:17683\n4034#10,6:17723\n4034#10,6:17777\n4034#10,6:17818\n4034#10,6:17864\n4034#10,6:17896\n4034#10,6:17945\n4034#10,6:17985\n4034#10,6:18017\n4034#10,6:18078\n4034#10,6:18118\n4034#10,6:18150\n4034#10,6:18214\n4034#10,6:18250\n4034#10,6:18295\n4034#10,6:18375\n4034#10,6:18407\n4034#10,6:18451\n4034#10,6:18511\n4034#10,6:18548\n4034#10,6:18595\n4034#10,6:18633\n4034#10,6:18670\n4034#10,6:18712\n4034#10,6:18754\n4034#10,6:18805\n4034#10,6:18842\n4034#10,6:18892\n4034#10,6:18937\n4034#10,6:18974\n4034#10,6:19021\n4034#10,6:19059\n4034#10,6:19096\n4034#10,6:19138\n4034#10,6:19180\n4034#10,6:19230\n4034#10,6:19278\n4034#10,6:19332\n4034#10,6:19364\n4034#10,6:19400\n4034#10,6:19436\n4034#10,6:19477\n4034#10,6:19519\n4034#10,6:19556\n4034#10,6:19605\n4034#10,6:19660\n4034#10,6:19714\n4034#10,6:19762\n4034#10,6:19799\n4034#10,6:19831\n4034#10,6:19867\n4034#10,6:19904\n4034#10,6:19946\n4034#10,6:19990\n4034#10,6:20043\n4034#10,6:20079\n4034#10,6:20111\n4034#10,6:20147\n4034#10,6:20191\n4034#10,6:20232\n4034#10,6:20275\n4034#10,6:20325\n4034#10,6:20364\n4034#10,6:20401\n4034#10,6:20448\n4034#10,6:20486\n4034#10,6:20523\n4034#10,6:20582\n4034#10,6:20619\n4034#10,6:20667\n4034#10,6:20711\n4034#10,6:20743\n86#11:7168\n83#11,6:7169\n89#11:7203\n93#11:7235\n86#11:7729\n83#11,6:7730\n89#11:7764\n86#11:8253\n83#11,6:8254\n89#11:8288\n93#11:8360\n86#11:8488\n82#11,7:8489\n89#11:8524\n93#11:8528\n86#11:8705\n82#11,7:8706\n89#11:8741\n93#11:8745\n93#11:8813\n86#11:8961\n82#11,7:8962\n89#11:8997\n93#11:9002\n86#11:9352\n83#11,6:9353\n89#11:9387\n86#11:9507\n83#11,6:9508\n89#11:9542\n93#11:9546\n93#11:9740\n86#11:10097\n83#11,6:10098\n89#11:10132\n93#11:10190\n86#11:10507\n83#11,6:10508\n89#11:10542\n93#11:10546\n86#11:10641\n83#11,6:10642\n89#11:10676\n93#11:10680\n86#11:10949\n83#11,6:10950\n89#11:10984\n93#11:11031\n86#11:11168\n83#11,6:11169\n89#11:11203\n93#11:11208\n86#11:11283\n83#11,6:11284\n89#11:11318\n93#11:11323\n86#11:11358\n83#11,6:11359\n89#11:11393\n86#11:11602\n83#11,6:11603\n89#11:11637\n93#11:11644\n86#11:11685\n82#11,7:11686\n89#11:11721\n93#11:11773\n93#11:11923\n86#11:11925\n83#11,6:11926\n89#11:11960\n86#11:12255\n83#11,6:12256\n89#11:12290\n93#11:12381\n86#11:12422\n82#11,7:12423\n89#11:12458\n93#11:12463\n86#11:12509\n83#11,6:12510\n89#11:12544\n86#11:12577\n82#11,7:12578\n89#11:12613\n93#11:12617\n86#11:12618\n82#11,7:12619\n89#11:12654\n93#11:12658\n93#11:12747\n93#11:13200\n86#11:13370\n83#11,6:13371\n89#11:13405\n93#11:13853\n86#11:13856\n83#11,6:13857\n89#11:13891\n93#11:14423\n86#11:14425\n83#11,6:14426\n89#11:14460\n86#11:14542\n83#11,6:14543\n89#11:14577\n93#11:14743\n86#11:14821\n83#11,6:14822\n89#11:14856\n93#11:14860\n86#11:14918\n83#11,6:14919\n89#11:14953\n86#11:14986\n83#11,6:14987\n89#11:15021\n93#11:15025\n93#11:15042\n86#11:15205\n82#11,7:15206\n89#11:15241\n93#11:15246\n93#11:15257\n86#11:15297\n83#11,6:15298\n89#11:15332\n93#11:15337\n86#11:15339\n84#11,5:15340\n89#11:15373\n86#11:15520\n82#11,7:15521\n89#11:15556\n93#11:15638\n86#11:15744\n82#11,7:15745\n89#11:15780\n93#11:15834\n93#11:15842\n86#11:15851\n82#11,7:15852\n89#11:15887\n93#11:15943\n86#11:15953\n82#11,7:15954\n89#11:15989\n93#11:16000\n86#11:16032\n83#11,6:16033\n89#11:16067\n93#11:16136\n86#11:16240\n83#11,6:16241\n89#11:16275\n86#11:16356\n82#11,7:16357\n89#11:16392\n93#11:16396\n86#11:16559\n82#11,7:16560\n89#11:16595\n93#11:16599\n86#11:16661\n83#11,6:16662\n89#11:16696\n93#11:16742\n86#11:16743\n82#11,7:16744\n89#11:16779\n93#11:16828\n93#11:16836\n86#11:16844\n83#11,6:16845\n89#11:16879\n86#11:16960\n82#11,7:16961\n89#11:16996\n93#11:17000\n93#11:17264\n86#11:17294\n84#11,5:17295\n89#11:17328\n86#11:17398\n82#11,7:17399\n89#11:17434\n93#11:17439\n86#11:17526\n83#11,6:17527\n89#11:17561\n93#11:17656\n86#11:17657\n83#11,6:17658\n89#11:17692\n93#11:17746\n86#11:17792\n83#11,6:17793\n89#11:17827\n93#11:17918\n86#11:17919\n83#11,6:17920\n89#11:17954\n93#11:18051\n86#11:18052\n83#11,6:18053\n89#11:18087\n93#11:18184\n86#11:18268\n82#11,7:18269\n89#11:18304\n93#11:18309\n93#11:18329\n86#11:18349\n83#11,6:18350\n89#11:18384\n93#11:18481\n86#11:18485\n83#11,6:18486\n89#11:18520\n86#11:18606\n82#11,7:18607\n89#11:18642\n93#11:18772\n93#11:18865\n86#11:18911\n83#11,6:18912\n89#11:18946\n86#11:19032\n82#11,7:19033\n89#11:19068\n93#11:19198\n86#11:19205\n84#11,5:19206\n89#11:19239\n93#11:19247\n93#11:19251\n86#11:19492\n82#11,7:19493\n89#11:19528\n93#11:19682\n86#11:19687\n82#11,7:19688\n89#11:19723\n93#11:19727\n86#11:19736\n83#11,6:19737\n89#11:19771\n86#11:19919\n82#11,7:19920\n89#11:19955\n93#11:19959\n86#11:20053\n83#11,6:20054\n89#11:20088\n86#11:20121\n83#11,6:20122\n89#11:20156\n93#11:20161\n93#11:20293\n86#11:20374\n82#11,7:20375\n89#11:20410\n93#11:20417\n86#11:20496\n82#11,7:20497\n89#11:20532\n93#11:20538\n93#11:20640\n86#11:20685\n83#11,6:20686\n89#11:20720\n93#11:20766\n149#12:7204\n149#12:7205\n149#12:7224\n149#12:7225\n149#12:7390\n159#12:7391\n149#12:7392\n149#12:7399\n149#12:7400\n149#12:7419\n159#12:7420\n149#12:7421\n149#12:7428\n149#12:7429\n149#12:7765\n149#12:7844\n149#12:7845\n149#12:7846\n149#12:7847\n149#12:7921\n149#12:7922\n149#12:7963\n149#12:7964\n149#12:7971\n159#12:7972\n149#12:7973\n149#12:7974\n149#12:7975\n149#12:7976\n149#12:7977\n149#12:8057\n149#12:8058\n149#12:8100\n149#12:8111\n149#12:8112\n149#12:8119\n149#12:8193\n149#12:8206\n149#12:8252\n149#12:8289\n149#12:8290\n159#12:8291\n149#12:8292\n149#12:8299\n149#12:8300\n149#12:8333\n149#12:8338\n149#12:8365\n149#12:8366\n149#12:8373\n149#12:8374\n149#12:8381\n149#12:8487\n149#12:8570\n149#12:8571\n149#12:8584\n149#12:8585\n149#12:8628\n149#12:8629\n149#12:8630\n149#12:8637\n149#12:8783\n149#12:8784\n149#12:8801\n149#12:8802\n149#12:8803\n149#12:8804\n159#12:8805\n149#12:8806\n149#12:8807\n149#12:8808\n149#12:8809\n149#12:8917\n149#12:8918\n149#12:8925\n149#12:8998\n149#12:9007\n149#12:9008\n149#12:9009\n149#12:9010\n149#12:9011\n149#12:9018\n149#12:9055\n149#12:9093\n149#12:9094\n149#12:9103\n149#12:9169\n149#12:9170\n149#12:9189\n149#12:9388\n149#12:9389\n149#12:9462\n149#12:9463\n149#12:9464\n149#12:9465\n149#12:9501\n149#12:9506\n149#12:9561\n149#12:9562\n149#12:9563\n149#12:9570\n149#12:9571\n149#12:9578\n159#12:9579\n149#12:9580\n149#12:9581\n149#12:9582\n149#12:9583\n149#12:9584\n149#12:9591\n159#12:9592\n149#12:9593\n149#12:9594\n149#12:9595\n149#12:9596\n149#12:9597\n149#12:9640\n149#12:9641\n159#12:9642\n149#12:9643\n149#12:9722\n149#12:9723\n149#12:9736\n149#12:10022\n149#12:10055\n149#12:10056\n149#12:10092\n149#12:10133\n149#12:10134\n149#12:10135\n149#12,11:10136\n149#12:10191\n149#12:10229\n149#12:10230\n149#12:10276\n149#12:10277\n149#12:10319\n149#12:10324\n149#12:10325\n149#12:10385\n149#12:10421\n159#12:10422\n149#12:10423\n149#12:10430\n149#12:10504\n149#12:10505\n149#12:10506\n149#12:10555\n159#12:10556\n149#12:10557\n149#12:10564\n149#12:10638\n149#12:10639\n149#12:10640\n149#12:10717\n159#12:10718\n149#12:10719\n149#12:10726\n149#12:10800\n149#12:10815\n149#12:10828\n149#12:10864\n159#12:10865\n149#12:10866\n149#12:10873\n149#12:10946\n149#12:10947\n149#12:10948\n149#12:11022\n149#12:11027\n149#12:11032\n149#12:11033\n159#12:11034\n149#12:11070\n149#12:11083\n159#12:11084\n149#12:11085\n149#12:11092\n149#12:11165\n149#12:11166\n149#12:11167\n149#12:11204\n149#12:11209\n149#12:11210\n159#12:11211\n149#12:11247\n149#12:11282\n149#12:11319\n149#12:11357\n149#12:11426\n149#12:11464\n149#12:11465\n149#12:11466\n149#12:11507\n149#12:11508\n149#12:11509\n149#12:11554\n149#12:11555\n149#12:11556\n149#12:11601\n149#12:11638\n149#12:11639\n149#12:11640\n149#12:11645\n149#12:11646\n149#12:11647\n149#12:11648\n149#12:11761\n149#12:11768\n149#12:11769\n149#12:11778\n149#12:11811\n149#12:11818\n149#12:11855\n149#12:11856\n149#12:11857\n149#12:11862\n159#12:11863\n149#12:11864\n149#12:11871\n149#12:11916\n149#12:11917\n159#12:11918\n149#12:11919\n149#12:11924\n149#12:12030\n149#12:12031\n149#12:12032\n149#12:12106\n149#12:12115\n149#12:12116\n149#12:12117\n149#12:12118\n149#12:12159\n149#12:12160\n149#12:12161\n149#12:12162\n149#12:12244\n149#12:12245\n149#12:12254\n149#12:12328\n149#12:12329\n149#12:12334\n149#12:12372\n149#12:12373\n149#12:12382\n149#12:12383\n159#12:12384\n149#12:12385\n149#12:12386\n149#12:12459\n149#12:12468\n149#12:12469\n159#12:12470\n149#12:12471\n149#12:12472\n149#12:12663\n149#12:12664\n149#12:12665\n149#12:12702\n149#12:12707\n149#12:12752\n149#12:12753\n149#12:12754\n149#12:12761\n149#12:12867\n149#12:12880\n149#12:12881\n149#12:12882\n149#12:12883\n149#12:12924\n149#12:12925\n149#12:12958\n149#12:12959\n149#12:12960\n149#12:13039\n149#12:13040\n149#12:13049\n159#12:13050\n149#12:13092\n149#12:13097\n159#12:13098\n149#12:13140\n149#12:13145\n159#12:13146\n149#12:13188\n149#12:13288\n149#12:13328\n149#12:13364\n149#12:13369\n149#12:13406\n149#12:13407\n149#12:13408\n149#12:13409\n149#12:13416\n149#12:13490\n149#12:13491\n149#12:13500\n149#12:13533\n149#12:13534\n149#12:13535\n149#12:13576\n149#12:13577\n149#12:13584\n149#12:13621\n149#12:13622\n149#12:13623\n149#12:13632\n149#12:13665\n149#12:13666\n149#12:13667\n149#12:13741\n149#12:13742\n149#12:13743\n149#12:13835\n149#12:13854\n149#12:13855\n149#12:13961\n149#12:13962\n149#12:13967\n149#12:13968\n149#12:13969\n149#12:14014\n149#12:14015\n149#12:14048\n149#12:14094\n149#12:14140\n149#12:14186\n149#12:14232\n149#12:14278\n149#12:14279\n149#12:14329\n149#12:14330\n149#12:14331\n149#12:14410\n149#12:14411\n149#12:14424\n149#12:14461\n149#12:14501\n149#12:14502\n149#12:14503\n149#12:14504\n149#12:14505\n149#12:14610\n149#12:14611\n149#12:14652\n149#12:14653\n149#12:14698\n149#12:14699\n149#12:14739\n149#12:14748\n149#12:14749\n159#12:14750\n149#12:14751\n149#12:14752\n149#12:14861\n149#12:14862\n149#12:14877\n149#12:14878\n159#12:14879\n149#12:14880\n149#12:14881\n149#12:15026\n149#12:15027\n149#12:15038\n149#12:15047\n149#12:15048\n149#12:15049\n149#12:15107\n149#12:15108\n149#12:15166\n149#12:15167\n149#12:15168\n149#12:15242\n149#12:15251\n149#12:15252\n149#12:15253\n149#12:15258\n149#12:15259\n159#12:15260\n149#12:15261\n149#12:15287\n149#12:15288\n149#12:15289\n149#12:15296\n149#12:15333\n149#12:15338\n149#12:15374\n149#12:15381\n149#12:15388\n149#12:15389\n149#12:15390\n149#12:15464\n149#12:15473\n149#12:15480\n149#12:15481\n149#12:15482\n149#12:15519\n149#12:15589\n149#12:15590\n149#12:15649\n149#12:15692\n149#12:15697\n159#12:15698\n149#12:15699\n149#12:15700\n149#12:15707\n149#12:15824\n149#12:15829\n149#12:15830\n149#12:15843\n149#12:15850\n149#12:15888\n149#12:15927\n149#12:15928\n149#12:15990\n149#12:16031\n149#12:16104\n149#12:16105\n149#12:16112\n149#12:16113\n149#12:16120\n149#12:16124\n149#12:16125\n149#12:16132\n149#12:16185\n149#12:16186\n149#12:16222\n149#12:16239\n149#12:16312\n149#12:16313\n159#12:16314\n149#12:16315\n149#12:16355\n149#12:16401\n149#12:16402\n159#12:16403\n149#12:16404\n149#12:16411\n149#12:16517\n149#12:16518\n149#12:16558\n149#12:16612\n149#12:16613\n149#12:16614\n149#12:16621\n149#12:16622\n149#12:16623\n149#12:16624\n149#12:16660\n149#12:16697\n149#12:16737\n149#12:16738\n149#12:16780\n149#12:16781\n149#12:16782\n149#12:16783\n149#12:16784\n149#12:16843\n149#12:16916\n149#12:16917\n159#12:16918\n149#12:16919\n149#12:16959\n149#12:17005\n149#12:17006\n149#12:17007\n149#12:17014\n149#12:17015\n149#12:17052\n149#12:17053\n149#12:17054\n149#12:17061\n149#12:17106\n149#12:17107\n149#12:17108\n149#12:17115\n149#12:17116\n149#12:17123\n149#12:17159\n149#12:17160\n149#12:17161\n149#12:17168\n149#12:17208\n149#12:17209\n149#12:17210\n149#12:17217\n149#12:17292\n149#12:17293\n149#12:17397\n149#12:17435\n149#12:17444\n149#12:17445\n149#12:17481\n149#12:17490\n149#12:17603\n149#12:17604\n149#12:17605\n149#12:17606\n149#12:17607\n149#12:17693\n149#12:17694\n149#12:17695\n149#12:17696\n149#12:17697\n149#12:17787\n149#12:17828\n149#12:17829\n149#12:17830\n149#12:17831\n149#12:17838\n149#12:17906\n149#12:17955\n149#12:17956\n149#12:17957\n149#12:17958\n149#12:17959\n149#12:18039\n149#12:18088\n149#12:18089\n149#12:18090\n149#12:18091\n149#12:18092\n149#12:18172\n149#12:18185\n149#12:18186\n149#12:18187\n149#12:18260\n149#12:18267\n149#12:18305\n149#12:18324\n149#12:18325\n149#12:18330\n149#12:18331\n149#12:18332\n149#12:18348\n149#12:18417\n149#12:18422\n149#12:18423\n149#12:18424\n149#12:18425\n149#12:18471\n149#12:18482\n149#12:18483\n149#12:18484\n149#12:18521\n159#12:18522\n149#12:18562\n149#12:18563\n149#12:18564\n149#12:18565\n149#12:18566\n149#12:18567\n149#12:18568\n149#12:18605\n149#12:18680\n149#12:18722\n149#12:18764\n149#12:18777\n149#12:18778\n149#12:18779\n149#12:18852\n149#12:18861\n149#12:18866\n149#12:18902\n149#12:18903\n159#12:18904\n149#12:18905\n149#12:18910\n149#12:18947\n159#12:18948\n149#12:18988\n149#12:18989\n149#12:18990\n149#12:18991\n149#12:18992\n149#12:18993\n149#12:18994\n149#12:19031\n149#12:19106\n149#12:19148\n149#12:19190\n149#12:19203\n149#12:19204\n149#12:19240\n149#12:19241\n149#12:19242\n149#12:19243\n149#12:19252\n149#12:19288\n149#12:19289\n159#12:19290\n149#12:19291\n149#12:19302\n159#12:19303\n149#12:19304\n149#12:19305\n149#12:19410\n149#12:19450\n149#12:19451\n149#12:19491\n149#12:19566\n149#12:19567\n149#12,11:19568\n149#12:19619\n149#12:19620\n159#12:19621\n149#12:19622\n149#12,11:19623\n149#12:19678\n149#12:19772\n149#12:19877\n159#12:19878\n149#12:19918\n149#12:19964\n149#12:20000\n149#12:20013\n149#12:20014\n149#12:20015\n149#12:20016\n149#12:20157\n149#12:20162\n149#12:20163\n149#12:20164\n149#12:20209\n149#12:20246\n149#12:20247\n149#12:20248\n149#12:20285\n149#12:20298\n159#12:20411\n149#12:20412\n149#12:20413\n149#12:20422\n149#12:20458\n149#12:20459\n149#12:20460\n149#12:20533\n149#12:20534\n149#12:20547\n149#12:20548\n149#12:20555\n149#12:20592\n149#12:20593\n149#12:20641\n149#12:20677\n159#12:20678\n149#12:20679\n149#12:20684\n149#12:20753\n149#12:20758\n149#12:20759\n149#12:20760\n149#12:20761\n149#12:20762\n149#12:20767\n149#12:20768\n159#12:20769\n149#12:20770\n99#13:7802\n96#13,6:7803\n102#13:7837\n99#13:7884\n95#13,7:7885\n102#13:7920\n106#13:7926\n106#13:7958\n99#13,3:8025\n102#13:8056\n99#13:8156\n95#13,7:8157\n102#13:8192\n106#13:8197\n106#13:8205\n99#13,3:8301\n102#13:8332\n106#13:8337\n99#13,3:8418\n102#13:8449\n99#13:8450\n95#13,7:8451\n102#13:8486\n106#13:8532\n99#13:8533\n95#13,7:8534\n102#13:8569\n106#13:8575\n106#13:8579\n99#13,3:8673\n102#13:8704\n99#13:8746\n95#13,7:8747\n102#13:8782\n106#13:8788\n106#13:8792\n99#13:9056\n95#13,7:9057\n102#13:9092\n106#13:9098\n99#13:9426\n96#13,6:9427\n102#13:9461\n106#13:9556\n99#13:9685\n95#13,7:9686\n102#13:9721\n106#13:9727\n99#13,3:10023\n102#13:10054\n99#13:10192\n95#13,7:10193\n102#13:10228\n106#13:10374\n106#13:10378\n99#13:10386\n97#13,5:10387\n102#13:10420\n99#13:10467\n95#13,7:10468\n102#13:10503\n106#13:10550\n99#13:10601\n95#13,7:10602\n102#13:10637\n106#13:10684\n106#13:10692\n99#13:10763\n95#13,7:10764\n102#13:10799\n106#13:10804\n99#13:10829\n97#13,5:10830\n102#13:10863\n99#13:10910\n96#13,6:10911\n102#13:10945\n99#13:10985\n95#13,7:10986\n102#13:11021\n106#13:11026\n106#13:11078\n99#13:11129\n96#13,6:11130\n102#13:11164\n106#13:11255\n106#13:11263\n99#13,3:11394\n102#13:11425\n99#13:11427\n95#13,7:11428\n102#13:11463\n106#13:11553\n106#13:11600\n99#13:11722\n97#13,5:11723\n102#13:11756\n106#13:11760\n99#13,3:11779\n102#13:11810\n99#13:11819\n96#13,6:11820\n102#13:11854\n106#13:11861\n106#13:11915\n99#13,3:11961\n102#13:11992\n99#13:11993\n95#13,7:11994\n102#13:12029\n99#13:12069\n95#13,7:12070\n102#13:12105\n106#13:12110\n106#13:12206\n99#13:12207\n95#13,7:12208\n102#13:12243\n106#13:12249\n106#13:12253\n99#13:12291\n95#13,7:12292\n102#13:12327\n106#13:12333\n99#13:12335\n95#13,7:12336\n102#13:12371\n106#13:12377\n99#13,3:12545\n102#13:12576\n106#13:12662\n99#13,3:12708\n102#13:12739\n106#13:12743\n99#13,3:12798\n102#13:12829\n99#13:12830\n95#13,7:12831\n102#13:12866\n106#13:12871\n106#13:12875\n99#13,3:12926\n102#13:12957\n99#13:13002\n95#13,7:13003\n102#13:13038\n106#13:13044\n106#13:13196\n99#13:13453\n95#13,7:13454\n102#13:13489\n106#13:13495\n99#13,3:13501\n102#13:13532\n99#13:13585\n96#13,6:13586\n102#13:13620\n106#13:13627\n106#13:13631\n99#13,3:13633\n102#13:13664\n99#13:13704\n95#13,7:13705\n102#13:13740\n106#13:13787\n99#13:13792\n95#13,7:13793\n102#13:13828\n106#13:13845\n106#13:13849\n99#13,3:13892\n102#13:13923\n99#13:13924\n95#13,7:13925\n102#13:13960\n106#13:13966\n106#13:14013\n99#13,3:14016\n102#13:14047\n106#13:14328\n99#13:14373\n95#13,7:14374\n102#13:14409\n106#13:14415\n99#13,3:14578\n102#13:14609\n106#13:14697\n99#13:14700\n97#13,5:14701\n102#13:14734\n106#13:14738\n99#13,3:14789\n102#13:14820\n106#13:14872\n99#13,3:14954\n102#13:14985\n106#13:15037\n99#13:15050\n97#13,5:15051\n102#13:15084\n106#13:15106\n99#13:15109\n97#13,5:15110\n102#13:15143\n106#13:15165\n99#13:15427\n95#13,7:15428\n102#13:15463\n106#13:15468\n99#13,3:15557\n102#13:15588\n106#13:15634\n99#13:15650\n96#13,6:15651\n102#13:15685\n106#13:15696\n99#13:15781\n95#13,7:15782\n102#13:15817\n106#13:15828\n99#13:15889\n95#13,7:15890\n102#13:15925\n106#13:15939\n99#13,3:16068\n102#13:16099\n106#13:16103\n99#13:16276\n96#13,6:16277\n102#13:16311\n106#13:16400\n99#13,3:16448\n102#13:16479\n99#13:16480\n95#13,7:16481\n102#13:16516\n106#13:16603\n106#13:16607\n99#13:16880\n96#13,6:16881\n102#13:16915\n106#13:17004\n99#13:17016\n97#13,5:17017\n102#13:17050\n106#13:17105\n99#13:17124\n97#13,5:17125\n102#13:17158\n106#13:17260\n99#13,3:17329\n102#13:17360\n99#13:17361\n96#13,6:17362\n102#13:17396\n106#13:17443\n106#13:17489\n99#13:17491\n97#13,5:17492\n102#13:17525\n99#13:17562\n95#13,7:17563\n102#13:17598\n106#13:17602\n106#13:17750\n99#13:17751\n96#13,6:17752\n102#13:17786\n106#13:17791\n99#13,3:17874\n102#13:17905\n106#13:17910\n99#13,3:17995\n102#13:18026\n106#13:18043\n99#13,3:18128\n102#13:18159\n106#13:18176\n99#13:18224\n96#13,6:18225\n102#13:18259\n106#13:18313\n99#13,3:18385\n102#13:18416\n106#13:18421\n99#13:18643\n95#13,7:18644\n102#13:18679\n106#13:18684\n99#13:18685\n95#13,7:18686\n102#13:18721\n106#13:18726\n99#13:18727\n95#13,7:18728\n102#13:18763\n106#13:18768\n99#13:18815\n95#13,7:18816\n102#13:18851\n106#13:18856\n99#13:19069\n95#13,7:19070\n102#13:19105\n106#13:19110\n99#13:19111\n95#13,7:19112\n102#13:19147\n106#13:19152\n99#13:19153\n95#13,7:19154\n102#13:19189\n106#13:19194\n99#13,3:19342\n102#13:19373\n99#13:19374\n96#13,6:19375\n102#13:19409\n99#13:19529\n95#13,7:19530\n102#13:19565\n106#13:19677\n106#13:19686\n106#13:19731\n99#13,3:19809\n102#13:19840\n99#13:19841\n96#13,6:19842\n102#13:19876\n106#13:19963\n106#13:20008\n99#13,3:20089\n102#13:20120\n106#13:20208\n99#13,3:20210\n102#13:20241\n106#13:20245\n99#13,3:20721\n102#13:20752\n106#13:20757\n1#14:8910\n81#15:9929\n107#15,2:9930\n81#15:9932\n107#15,2:9933\n81#15:9935\n107#15,2:9936\n81#15:9938\n107#15,2:9939\n81#15:9941\n107#15,2:9942\n81#15:9944\n107#15,2:9945\n81#15:9953\n107#15,2:9954\n81#15:9956\n107#15,2:9957\n81#15:9959\n107#15,2:9960\n81#15:9962\n107#15,2:9963\n81#15:9965\n107#15,2:9966\n81#15:9968\n107#15,2:9969\n81#15:9971\n107#15,2:9972\n81#15:9974\n107#15,2:9975\n81#15:9977\n107#15,2:9978\n81#15:9980\n107#15,2:9981\n81#15:9983\n107#15,2:9984\n81#15:9986\n107#15,2:9987\n81#15:9989\n107#15,2:9990\n81#15:9992\n107#15,2:9993\n81#15:9995\n107#15,2:9996\n81#15:9998\n107#15,2:9999\n81#15:10001\n107#15,2:10002\n81#15:10007\n107#15,2:10008\n81#15:10010\n107#15,2:10011\n81#15:10013\n107#15,2:10014\n81#15:10016\n107#15,2:10017\n81#15:10019\n107#15,2:10020\n81#15:13201\n107#15,2:13202\n81#15:13207\n107#15,2:13208\n81#15:13210\n107#15,2:13211\n81#15:13213\n107#15,2:13214\n81#15:13216\n107#15,2:13217\n81#15:13219\n107#15,2:13220\n81#15:13222\n107#15,2:13223\n81#15:13225\n107#15,2:13226\n81#15:13228\n107#15,2:13229\n81#15:13231\n107#15,2:13232\n81#15:13234\n107#15,2:13235\n81#15:13237\n107#15,2:13238\n81#15:13249\n107#15,2:13250\n81#15:13252\n107#15,2:13253\n81#15:13258\n107#15,2:13259\n81#15:13261\n107#15,2:13262\n81#15:13264\n107#15,2:13265\n81#15:13267\n107#15,2:13268\n81#15:13270\n107#15,2:13271\n81#15:13273\n107#15,2:13274\n81#15:13285\n107#15,2:13286\n81#15:15262\n81#15:15263\n107#15,2:15264\n81#15:15266\n107#15,2:15267\n81#15:15269\n107#15,2:15270\n81#15:15272\n107#15,2:15273\n81#15:15275\n107#15,2:15276\n81#15:15278\n107#15,2:15279\n81#15:15281\n107#15,2:15282\n81#15:15284\n107#15,2:15285\n81#15:15944\n107#15,2:15945\n81#15:16001\n107#15,2:16002\n81#15:16004\n107#15,2:16005\n81#15:16137\n107#15,2:16138\n81#15:16143\n107#15,2:16144\n81#15:16146\n107#15,2:16147\n81#15:16149\n107#15,2:16150\n81#15:16152\n107#15,2:16153\n81#15:16155\n107#15,2:16156\n81#15:16158\n107#15,2:16159\n81#15:16161\n107#15,2:16162\n81#15:16164\n107#15,2:16165\n81#15:16167\n107#15,2:16168\n81#15:16170\n107#15,2:16171\n81#15:16173\n107#15,2:16174\n81#15:16176\n107#15,2:16177\n81#15:16179\n107#15,2:16180\n81#15:16182\n107#15,2:16183\n81#15:17265\n107#15,2:17266\n81#15:17268\n107#15,2:17269\n81#15:17271\n107#15,2:17272\n81#15:17274\n107#15,2:17275\n81#15:17277\n107#15,2:17278\n81#15:17280\n107#15,2:17281\n81#15:17283\n107#15,2:17284\n81#15:17286\n107#15,2:17287\n81#15:17289\n107#15,2:17290\n81#15:18333\n107#15,2:18334\n81#15:19296\n107#15,2:19297\n81#15:19299\n107#15,2:19300\n78#16:9947\n111#16,2:9948\n78#16:9950\n111#16,2:9951\n78#16:10004\n111#16,2:10005\n78#16:13204\n111#16,2:13205\n78#16:13240\n111#16,2:13241\n78#16:13243\n111#16,2:13244\n78#16:13279\n111#16,2:13280\n78#16:16140\n111#16,2:16141\n179#17,12:11345\n143#17,12:16007\n143#17,12:16019\n143#17,12:16227\n143#17,12:18336\n78#18:13246\n111#18,2:13247\n78#18:13255\n111#18,2:13256\n78#18:13276\n111#18,2:13277\n78#18:13282\n111#18,2:13283\n434#19:16837\n507#19,5:16838\n*S KotlinDebug\n*F\n+ 1 LiveDhikrScreen.kt\ncom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt\n*L\n211#1:6822\n235#1:6829\n236#1:6830\n2037#1:7436\n2039#1:7437\n3271#1:8908\n3272#1:8909\n3861#1:9104\n3862#1:9105\n5365#1:9775\n212#1:6823,6\n237#1:6836,3\n237#1:6842,3\n238#1:6846,6\n240#1:6852,6\n241#1:6858,6\n242#1:6864,6\n244#1:6870,6\n245#1:6876,6\n246#1:6882,6\n247#1:6888,6\n248#1:6894,6\n249#1:6900,6\n250#1:6906,6\n251#1:6912,6\n254#1:6918,6\n255#1:6924,6\n256#1:6930,6\n257#1:6936,6\n258#1:6942,6\n259#1:6948,6\n260#1:6954,6\n261#1:6960,6\n263#1:6966,6\n264#1:6972,6\n265#1:6978,6\n266#1:6984,6\n269#1:6990,6\n288#1:6996,3\n288#1:7003,3\n294#1:7006,6\n314#1:7012,6\n323#1:7018,6\n324#1:7024,6\n339#1:7030,6\n352#1:7036,6\n356#1:7042,6\n357#1:7048,6\n359#1:7054,6\n351#1:7060,6\n371#1:7066,6\n372#1:7072,6\n373#1:7078,6\n374#1:7084,6\n375#1:7090,6\n376#1:7096,6\n377#1:7102,6\n382#1:7108,6\n391#1:7114,6\n402#1:7120,6\n420#1:7126,6\n563#1:7206,6\n576#1:7212,6\n580#1:7218,6\n589#1:7226,6\n1044#1:7236,6\n1072#1:7242,6\n1079#1:7248,6\n1090#1:7254,6\n1096#1:7260,6\n1101#1:7266,6\n1109#1:7272,6\n1110#1:7278,6\n1145#1:7284,6\n1149#1:7290,6\n1158#1:7296,6\n1159#1:7302,6\n1190#1:7308,6\n1191#1:7314,6\n1202#1:7320,6\n1203#1:7326,6\n1218#1:7332,6\n1230#1:7338,6\n1237#1:7344,6\n1247#1:7350,6\n1257#1:7356,6\n1282#1:7362,6\n1290#1:7368,6\n1294#1:7374,6\n1310#1:7384,6\n1335#1:7393,6\n1584#1:7401,6\n1585#1:7407,6\n1594#1:7413,6\n1606#1:7422,6\n2035#1:7430,6\n2040#1:7443,3\n2040#1:7449,3\n2042#1:7453,6\n2043#1:7459,6\n2044#1:7465,6\n2047#1:7471,6\n2048#1:7477,6\n2049#1:7483,6\n2050#1:7489,6\n2051#1:7495,6\n2052#1:7501,6\n2054#1:7507,6\n2086#1:7513,6\n2109#1:7519,6\n2110#1:7525,6\n2111#1:7531,6\n2112#1:7537,6\n2113#1:7543,6\n2115#1:7549,6\n2118#1:7555,6\n2119#1:7561,6\n2120#1:7567,6\n2121#1:7573,6\n2124#1:7579,6\n2125#1:7585,6\n2126#1:7591,6\n2128#1:7597,6\n2129#1:7603,6\n2132#1:7609,6\n2133#1:7615,6\n2134#1:7621,6\n2146#1:7627,6\n2147#1:7633,6\n2148#1:7639,6\n2149#1:7645,6\n2150#1:7651,6\n2206#1:7657,6\n2235#1:7663,6\n2240#1:7669,6\n2246#1:7675,6\n2258#1:7681,6\n2274#1:7687,6\n2295#1:7838,6\n2355#1:7931,6\n2373#1:7937,6\n2389#1:7943,6\n2425#1:7949,6\n2452#1:7965,6\n2652#1:7978,6\n2660#1:8019,6\n2671#1:8059,6\n2677#1:8101,6\n2696#1:8113,6\n2724#1:8242,6\n2751#1:8293,6\n2777#1:8339,6\n2778#1:8345,6\n2779#1:8351,6\n2792#1:8367,6\n2796#1:8375,6\n2846#1:8586,6\n2859#1:8631,6\n3128#1:8814,6\n3129#1:8820,6\n3130#1:8826,6\n3138#1:8832,6\n3144#1:8838,6\n3154#1:8844,6\n3164#1:8850,6\n3174#1:8856,6\n3183#1:8862,6\n3190#1:8868,6\n3198#1:8874,6\n3217#1:8880,6\n3241#1:8886,6\n3249#1:8892,6\n3253#1:8898,6\n3275#1:8911,6\n3724#1:8919,6\n3796#1:9012,6\n3863#1:9111,3\n3863#1:9117,3\n3864#1:9121,6\n3865#1:9127,6\n3866#1:9133,6\n3867#1:9139,6\n3868#1:9145,6\n3869#1:9151,6\n3871#1:9157,6\n3872#1:9163,6\n4311#1:9171,6\n4401#1:9177,6\n4402#1:9183,6\n4584#1:9190,6\n4586#1:9196,6\n4587#1:9202,6\n4590#1:9208,6\n4591#1:9214,6\n4592#1:9220,6\n4593#1:9226,6\n4594#1:9232,6\n4596#1:9238,6\n4626#1:9244,6\n4649#1:9250,6\n4650#1:9256,6\n4651#1:9262,6\n4652#1:9268,6\n4653#1:9274,6\n4654#1:9280,6\n4655#1:9286,6\n4656#1:9292,6\n4657#1:9298,6\n4665#1:9304,6\n4670#1:9310,6\n4751#1:9547,6\n4796#1:9564,6\n4832#1:9572,6\n5050#1:9585,6\n5261#1:9598,6\n5274#1:9644,6\n5307#1:9741,6\n5308#1:9747,6\n5337#1:9753,6\n5345#1:9759,6\n5349#1:9765,6\n5366#1:9776,6\n5367#1:9782,6\n5368#1:9788,6\n5369#1:9794,6\n5370#1:9800,6\n5371#1:9806,6\n5373#1:9812,6\n5374#1:9818,6\n5375#1:9824,6\n5377#1:9830,6\n5378#1:9836,6\n5379#1:9842,6\n5816#1:9848,6\n5822#1:9854,6\n5830#1:9860,6\n5835#1:9866,6\n5843#1:9872,6\n5847#1:9878,6\n5859#1:9884,6\n5860#1:9890,3\n5860#1:9896,3\n6296#1:9899,6\n6297#1:9905,6\n6298#1:9911,6\n6299#1:9917,6\n6301#1:9923,6\n508#1:10231,6\n527#1:10278,6\n545#1:10326,6\n595#1:10379,6\n610#1:10424,6\n658#1:10558,6\n711#1:10693,6\n721#1:10699,6\n731#1:10705,6\n741#1:10711,6\n756#1:10720,6\n701#1:10809,6\n703#1:10816,6\n781#1:10822,6\n799#1:10867,6\n863#1:11086,6\n919#1:11264,6\n931#1:11270,6\n938#1:11276,6\n1498#1:11762,6\n1520#1:11812,6\n1539#1:11865,6\n1873#1:12755,6\n1940#1:12961,6\n1970#1:13051,6\n1988#1:13099,6\n2007#1:13147,6\n2504#1:13410,6\n2561#1:13578,6\n2630#1:13829,6\n2638#1:13836,6\n2992#1:14049,6\n3006#1:14095,6\n3020#1:14141,6\n3034#1:14187,6\n3052#1:14233,6\n3067#1:14280,6\n3098#1:14332,6\n3481#1:14863,6\n3532#1:15028,6\n3580#1:15085,6\n3591#1:15091,6\n3602#1:15097,6\n3622#1:15144,6\n3635#1:15150,6\n3646#1:15156,6\n4123#1:15290,6\n3911#1:15375,6\n3930#1:15382,6\n3971#1:15474,6\n4025#1:15643,6\n4031#1:15686,6\n4067#1:15701,6\n4077#1:15818,6\n4290#1:15844,6\n4274#1:15929,6\n4330#1:15947,6\n4321#1:15991,6\n4441#1:16106,6\n4458#1:16114,6\n4511#1:16126,6\n4888#1:16405,6\n4943#1:16615,6\n5105#1:17008,6\n5144#1:17055,6\n5177#1:17109,6\n5202#1:17117,6\n5216#1:17162,6\n5239#1:17211,6\n5477#1:17643,6\n5521#1:17733,6\n5576#1:17832,6\n5627#1:18027,6\n5649#1:18033,6\n5690#1:18160,6\n5712#1:18166,6\n5742#1:18261,6\n5770#1:18318,6\n5900#1:18461,6\n5914#1:18472,6\n6581#1:20549,6\n237#1:6831\n237#1:6832,4\n237#1:6839,2\n237#1:6845\n2040#1:7438\n2040#1:7439,4\n2040#1:7446,2\n2040#1:7452\n3863#1:9106\n3863#1:9107,4\n3863#1:9114,2\n3863#1:9120\n237#1:6841\n2040#1:7448\n3863#1:9116\n289#1:6999,2\n290#1:7001,2\n5861#1:9893\n5861#1:9894,2\n951#1:11324\n951#1:11325,2\n952#1:11327\n952#1:11328,2\n953#1:11330\n953#1:11331,2\n955#1:11333\n955#1:11334,2\n956#1:11336\n956#1:11337,2\n960#1:11339\n960#1:11340,2\n962#1:11342\n962#1:11343,2\n4272#1:15926\n4272#1:15935\n4497#1:16121\n4497#1:16122,2\n5124#1:17051\n5124#1:17101\n417#1:7132\n417#1:7133,6\n417#1:7167\n417#1:7383\n2271#1:7693\n2271#1:7694,6\n2271#1:7728\n2284#1:7766\n2284#1:7767,6\n2284#1:7801\n2329#1:7848\n2329#1:7849,6\n2329#1:7883\n2329#1:7930\n2284#1:7962\n2648#1:7984\n2648#1:7985,5\n2648#1:8018\n2665#1:8065\n2665#1:8066,5\n2665#1:8099\n2665#1:8110\n2691#1:8120\n2691#1:8121,6\n2691#1:8155\n2691#1:8201\n2714#1:8207\n2714#1:8208,5\n2714#1:8241\n2714#1:8251\n2648#1:8364\n2788#1:8382\n2788#1:8383,6\n2788#1:8417\n2788#1:8583\n2842#1:8592\n2842#1:8593,6\n2842#1:8627\n2848#1:8638\n2848#1:8639,5\n2848#1:8672\n2848#1:8796\n2842#1:8800\n2271#1:8907\n3716#1:8926\n3716#1:8927,5\n3716#1:8960\n3716#1:9006\n3791#1:9019\n3791#1:9020,6\n3791#1:9054\n3791#1:9102\n4667#1:9316\n4667#1:9317,6\n4667#1:9351\n4680#1:9390\n4680#1:9391,6\n4", "680#1:9425\n4717#1:9466\n4717#1:9467,5\n4717#1:9500\n4717#1:9505\n4680#1:9560\n5257#1:9604\n5257#1:9605,6\n5257#1:9639\n5263#1:9650\n5263#1:9651,5\n5263#1:9684\n5263#1:9731\n5257#1:9735\n4667#1:9774\n449#1:10057\n449#1:10058,5\n449#1:10091\n449#1:10096\n480#1:10147\n480#1:10148,6\n480#1:10182\n480#1:10186\n502#1:10237\n502#1:10238,5\n502#1:10271\n502#1:10275\n521#1:10284\n521#1:10285,5\n521#1:10318\n521#1:10323\n539#1:10332\n539#1:10333,5\n539#1:10366\n539#1:10370\n600#1:10431\n600#1:10432,6\n600#1:10466\n600#1:10554\n648#1:10565\n648#1:10566,6\n648#1:10600\n648#1:10688\n747#1:10727\n747#1:10728,6\n747#1:10762\n747#1:10808\n788#1:10874\n788#1:10875,6\n788#1:10909\n834#1:11035\n834#1:11036,5\n834#1:11069\n834#1:11074\n788#1:11082\n852#1:11093\n852#1:11094,6\n852#1:11128\n894#1:11212\n894#1:11213,5\n894#1:11246\n894#1:11251\n852#1:11259\n1356#1:11467\n1356#1:11468,6\n1356#1:11502\n1356#1:11506\n1369#1:11510\n1369#1:11511,6\n1369#1:11545\n1369#1:11549\n1399#1:11557\n1399#1:11558,6\n1399#1:11592\n1399#1:11596\n1470#1:11649\n1470#1:11650,6\n1470#1:11684\n1470#1:11777\n1534#1:11872\n1534#1:11873,6\n1534#1:11907\n1534#1:11911\n1624#1:12033\n1624#1:12034,6\n1624#1:12068\n1624#1:12114\n1645#1:12119\n1645#1:12120,6\n1645#1:12154\n1645#1:12158\n1661#1:12163\n1661#1:12164,6\n1661#1:12198\n1661#1:12202\n1736#1:12387\n1736#1:12388,5\n1736#1:12421\n1736#1:12467\n1772#1:12473\n1772#1:12474,6\n1772#1:12508\n1821#1:12666\n1821#1:12667,6\n1821#1:12701\n1821#1:12706\n1772#1:12751\n1867#1:12762\n1867#1:12763,6\n1867#1:12797\n1867#1:12879\n1903#1:12884\n1903#1:12885,6\n1903#1:12919\n1903#1:12923\n1929#1:12967\n1929#1:12968,5\n1929#1:13001\n1929#1:13048\n1964#1:13057\n1964#1:13058,5\n1964#1:13091\n1964#1:13096\n1982#1:13105\n1982#1:13106,5\n1982#1:13139\n1982#1:13144\n2001#1:13153\n2001#1:13154,5\n2001#1:13187\n2001#1:13192\n2377#1:13289\n2377#1:13290,5\n2377#1:13323\n2377#1:13327\n2406#1:13329\n2406#1:13330,5\n2406#1:13363\n2406#1:13368\n2489#1:13417\n2489#1:13418,6\n2489#1:13452\n2489#1:13499\n2540#1:13536\n2540#1:13537,6\n2540#1:13571\n2540#1:13575\n2592#1:13668\n2592#1:13669,6\n2592#1:13703\n2607#1:13744\n2607#1:13745,6\n2607#1:13779\n2607#1:13783\n2592#1:13791\n2951#1:13970\n2951#1:13971,6\n2951#1:14005\n2951#1:14009\n2987#1:14055\n2987#1:14056,5\n2987#1:14089\n2987#1:14093\n3001#1:14101\n3001#1:14102,5\n3001#1:14135\n3001#1:14139\n3015#1:14147\n3015#1:14148,5\n3015#1:14181\n3015#1:14185\n3029#1:14193\n3029#1:14194,5\n3029#1:14227\n3029#1:14231\n3043#1:14239\n3043#1:14240,5\n3043#1:14273\n3043#1:14277\n3061#1:14286\n3061#1:14287,5\n3061#1:14320\n3061#1:14324\n3088#1:14338\n3088#1:14339,5\n3088#1:14372\n3088#1:14419\n3301#1:14462\n3301#1:14463,5\n3301#1:14496\n3301#1:14500\n3344#1:14506\n3344#1:14507,6\n3344#1:14541\n3368#1:14612\n3368#1:14613,6\n3368#1:14647\n3368#1:14651\n3382#1:14654\n3382#1:14655,6\n3382#1:14689\n3382#1:14693\n3344#1:14747\n3451#1:14753\n3451#1:14754,6\n3451#1:14788\n3451#1:14876\n3500#1:14882\n3500#1:14883,6\n3500#1:14917\n3500#1:15046\n3653#1:15169\n3653#1:15170,6\n3653#1:15204\n3653#1:15250\n3947#1:15391\n3947#1:15392,6\n3947#1:15426\n3947#1:15472\n3988#1:15483\n3988#1:15484,6\n3988#1:15518\n4003#1:15591\n4003#1:15592,6\n4003#1:15626\n4003#1:15630\n3988#1:15642\n4058#1:15708\n4058#1:15709,6\n4058#1:15743\n4058#1:15838\n4769#1:16187\n4769#1:16188,5\n4769#1:16221\n4769#1:16226\n4849#1:16316\n4849#1:16317,5\n4849#1:16350\n4849#1:16354\n4878#1:16412\n4878#1:16413,6\n4878#1:16447\n4897#1:16519\n4897#1:16520,5\n4897#1:16553\n4897#1:16557\n4878#1:16611\n4957#1:16625\n4957#1:16626,5\n4957#1:16659\n4975#1:16698\n4975#1:16699,5\n4975#1:16732\n4975#1:16736\n5022#1:16785\n5022#1:16786,6\n5022#1:16820\n5022#1:16824\n4957#1:16832\n5067#1:16920\n5067#1:16921,5\n5067#1:16954\n5067#1:16958\n5134#1:17062\n5134#1:17063,5\n5134#1:17096\n5134#1:17100\n5206#1:17169\n5206#1:17170,5\n5206#1:17203\n5206#1:17207\n5229#1:17218\n5229#1:17219,5\n5229#1:17252\n5229#1:17256\n5426#1:17446\n5426#1:17447,5\n5426#1:17480\n5426#1:17485\n5465#1:17608\n5465#1:17609,5\n5465#1:17642\n5465#1:17652\n5509#1:17698\n5509#1:17699,5\n5509#1:17732\n5509#1:17742\n5569#1:17839\n5569#1:17840,5\n5569#1:17873\n5569#1:17914\n5610#1:17960\n5610#1:17961,5\n5610#1:17994\n5610#1:18047\n5673#1:18093\n5673#1:18094,5\n5673#1:18127\n5673#1:18180\n5728#1:18188\n5728#1:18189,6\n5728#1:18223\n5728#1:18317\n5889#1:18426\n5889#1:18427,5\n5889#1:18460\n5889#1:18470\n5966#1:18523\n5966#1:18524,5\n5966#1:18557\n5966#1:18561\n6016#1:18569\n6016#1:18570,6\n6016#1:18604\n6016#1:18776\n6061#1:18780\n6061#1:18781,5\n6061#1:18814\n6061#1:18860\n5943#1:18867\n5943#1:18868,5\n5943#1:18901\n5943#1:18909\n6144#1:18949\n6144#1:18950,5\n6144#1:18983\n6144#1:18987\n6194#1:18995\n6194#1:18996,6\n6194#1:19030\n6194#1:19202\n6121#1:19253\n6121#1:19254,5\n6121#1:19287\n6121#1:19295\n6600#1:19306\n6600#1:19307,6\n6600#1:19341\n6618#1:19411\n6618#1:19412,5\n6618#1:19445\n6618#1:19449\n6640#1:19452\n6640#1:19453,5\n6640#1:19486\n6640#1:19490\n6669#1:19579\n6669#1:19580,6\n6669#1:19614\n6669#1:19618\n6684#1:19634\n6684#1:19635,6\n6684#1:19669\n6684#1:19673\n6600#1:19735\n6338#1:19773\n6338#1:19774,6\n6338#1:19808\n6357#1:19879\n6357#1:19880,5\n6357#1:19913\n6357#1:19917\n6383#1:19965\n6383#1:19966,5\n6383#1:19999\n6383#1:20004\n6338#1:20012\n6403#1:20017\n6403#1:20018,6\n6403#1:20052\n6439#1:20165\n6439#1:20166,6\n6439#1:20200\n6439#1:20204\n6478#1:20249\n6478#1:20250,6\n6478#1:20284\n6478#1:20289\n6403#1:20297\n6502#1:20299\n6502#1:20300,6\n6502#1:20334\n6502#1:20338\n6518#1:20339\n6518#1:20340,5\n6518#1:20373\n6518#1:20421\n6539#1:20423\n6539#1:20424,5\n6539#1:20457\n6546#1:20461\n6546#1:20462,5\n6546#1:20495\n6546#1:20542\n6539#1:20546\n6734#1:20556\n6734#1:20557,6\n6734#1:20591\n6739#1:20594\n6739#1:20595,5\n6739#1:20628\n6739#1:20632\n6734#1:20636\n6318#1:20642\n6318#1:20643,5\n6318#1:20676\n6318#1:20683\n417#1:7139,6\n417#1:7154,4\n417#1:7164,2\n423#1:7175,6\n423#1:7190,4\n423#1:7200,2\n423#1:7234\n417#1:7382\n2271#1:7700,6\n2271#1:7715,4\n2271#1:7725,2\n2276#1:7736,6\n2276#1:7751,4\n2276#1:7761,2\n2284#1:7773,6\n2284#1:7788,4\n2284#1:7798,2\n2290#1:7809,6\n2290#1:7824,4\n2290#1:7834,2\n2329#1:7855,6\n2329#1:7870,4\n2329#1:7880,2\n2335#1:7892,6\n2335#1:7907,4\n2335#1:7917,2\n2335#1:7925\n2329#1:7929\n2290#1:7957\n2284#1:7961\n2648#1:7990,6\n2648#1:8005,4\n2648#1:8015,2\n2656#1:8028,6\n2656#1:8043,4\n2656#1:8053,2\n2665#1:8071,6\n2665#1:8086,4\n2665#1:8096,2\n2665#1:8109\n2691#1:8127,6\n2691#1:8142,4\n2691#1:8152,2\n2699#1:8164,6\n2699#1:8179,4\n2699#1:8189,2\n2699#1:8196\n2691#1:8200\n2656#1:8204\n2714#1:8213,6\n2714#1:8228,4\n2714#1:8238,2\n2714#1:8250\n2728#1:8260,6\n2728#1:8275,4\n2728#1:8285,2\n2746#1:8304,6\n2746#1:8319,4\n2746#1:8329,2\n2746#1:8336\n2728#1:8359\n2648#1:8363\n2788#1:8389,6\n2788#1:8404,4\n2788#1:8414,2\n2799#1:8421,6\n2799#1:8436,4\n2799#1:8446,2\n2804#1:8458,6\n2804#1:8473,4\n2804#1:8483,2\n2807#1:8496,6\n2807#1:8511,4\n2807#1:8521,2\n2807#1:8527\n2804#1:8531\n2821#1:8541,6\n2821#1:8556,4\n2821#1:8566,2\n2821#1:8574\n2799#1:8578\n2788#1:8582\n2842#1:8599,6\n2842#1:8614,4\n2842#1:8624,2\n2848#1:8644,6\n2848#1:8659,4\n2848#1:8669,2\n2863#1:8676,6\n2863#1:8691,4\n2863#1:8701,2\n2868#1:8713,6\n2868#1:8728,4\n2868#1:8738,2\n2868#1:8744\n2882#1:8754,6\n2882#1:8769,4\n2882#1:8779,2\n2882#1:8787\n2863#1:8791\n2848#1:8795\n2842#1:8799\n2276#1:8812\n2271#1:8906\n3716#1:8932,6\n3716#1:8947,4\n3716#1:8957,2\n3728#1:8969,6\n3728#1:8984,4\n3728#1:8994,2\n3728#1:9001\n3716#1:9005\n3791#1:9026,6\n3791#1:9041,4\n3791#1:9051,2\n3799#1:9064,6\n3799#1:9079,4\n3799#1:9089,2\n3799#1:9097\n3791#1:9101\n4667#1:9323,6\n4667#1:9338,4\n4667#1:9348,2\n4672#1:9359,6\n4672#1:9374,4\n4672#1:9384,2\n4680#1:9397,6\n4680#1:9412,4\n4680#1:9422,2\n4691#1:9433,6\n4691#1:9448,4\n4691#1:9458,2\n4717#1:9472,6\n4717#1:9487,4\n4717#1:9497,2\n4717#1:9504\n4736#1:9514,6\n4736#1:9529,4\n4736#1:9539,2\n4736#1:9545\n4691#1:9555\n4680#1:9559\n5257#1:9611,6\n5257#1:9626,4\n5257#1:9636,2\n5263#1:9656,6\n5263#1:9671,4\n5263#1:9681,2\n5281#1:9693,6\n5281#1:9708,4\n5281#1:9718,2\n5281#1:9726\n5263#1:9730\n5257#1:9734\n4672#1:9739\n4667#1:9773\n441#1:10026,6\n441#1:10041,4\n441#1:10051,2\n449#1:10063,6\n449#1:10078,4\n449#1:10088,2\n449#1:10095\n467#1:10104,6\n467#1:10119,4\n467#1:10129,2\n480#1:10154,6\n480#1:10169,4\n480#1:10179,2\n480#1:10185\n467#1:10189\n497#1:10200,6\n497#1:10215,4\n497#1:10225,2\n502#1:10243,6\n502#1:10258,4\n502#1:10268,2\n502#1:10274\n521#1:10290,6\n521#1:10305,4\n521#1:10315,2\n521#1:10322\n539#1:10338,6\n539#1:10353,4\n539#1:10363,2\n539#1:10369\n497#1:10373\n441#1:10377\n592#1:10392,6\n592#1:10407,4\n592#1:10417,2\n600#1:10438,6\n600#1:10453,4\n600#1:10463,2\n613#1:10475,6\n613#1:10490,4\n613#1:10500,2\n624#1:10514,6\n624#1:10529,4\n624#1:10539,2\n624#1:10545\n613#1:10549\n600#1:10553\n648#1:10572,6\n648#1:10587,4\n648#1:10597,2\n661#1:10609,6\n661#1:10624,4\n661#1:10634,2\n672#1:10648,6\n672#1:10663,4\n672#1:10673,2\n672#1:10679\n661#1:10683\n648#1:10687\n592#1:10691\n747#1:10734,6\n747#1:10749,4\n747#1:10759,2\n759#1:10771,6\n759#1:10786,4\n759#1:10796,2\n759#1:10803\n747#1:10807\n777#1:10835,6\n777#1:10850,4\n777#1:10860,2\n788#1:10881,6\n788#1:10896,4\n788#1:10906,2\n802#1:10917,6\n802#1:10932,4\n802#1:10942,2\n812#1:10956,6\n812#1:10971,4\n812#1:10981,2\n813#1:10993,6\n813#1:11008,4\n813#1:11018,2\n813#1:11025\n812#1:11030\n834#1:11041,6\n834#1:11056,4\n834#1:11066,2\n834#1:11073\n802#1:11077\n788#1:11081\n852#1:11100,6\n852#1:11115,4\n852#1:11125,2\n866#1:11136,6\n866#1:11151,4\n866#1:11161,2\n876#1:11175,6\n876#1:11190,4\n876#1:11200,2\n876#1:11207\n894#1:11218,6\n894#1:11233,4\n894#1:11243,2\n894#1:11250\n866#1:11254\n852#1:11258\n777#1:11262\n975#1:11290,6\n975#1:11305,4\n975#1:11315,2\n975#1:11322\n1340#1:11365,6\n1340#1:11380,4\n1340#1:11390,2\n1346#1:11397,6\n1346#1:11412,4\n1346#1:11422,2\n1351#1:11435,6\n1351#1:11450,4\n1351#1:11460,2\n1356#1:11474,6\n1356#1:11489,4\n1356#1:11499,2\n1356#1:11505\n1369#1:11517,6\n1369#1:11532,4\n1369#1:11542,2\n1369#1:11548\n1351#1:11552\n1399#1:11564,6\n1399#1:11579,4\n1399#1:11589,2\n1399#1:11595\n1346#1:11599\n1418#1:11609,6\n1418#1:11624,4\n1418#1:11634,2\n1418#1:11643\n1470#1:11656,6\n1470#1:11671,4\n1470#1:11681,2\n1478#1:11693,6\n1478#1:11708,4\n1478#1:11718,2\n1479#1:11728,6\n1479#1:11743,4\n1479#1:11753,2\n1479#1:11759\n1478#1:11772\n1470#1:11776\n1512#1:11782,6\n1512#1:11797,4\n1512#1:11807,2\n1517#1:11826,6\n1517#1:11841,4\n1517#1:11851,2\n1517#1:11860\n1534#1:11879,6\n1534#1:11894,4\n1534#1:11904,2\n1534#1:11910\n1512#1:11914\n1340#1:11922\n1611#1:11932,6\n1611#1:11947,4\n1611#1:11957,2\n1617#1:11964,6\n1617#1:11979,4\n1617#1:11989,2\n1623#1:12001,6\n1623#1:12016,4\n1623#1:12026,2\n1624#1:12040,6\n1624#1:12055,4\n1624#1:12065,2\n1631#1:12077,6\n1631#1:12092,4\n1631#1:12102,2\n1631#1:12109\n1624#1:12113\n1645#1:12126,6\n1645#1:12141,4\n1645#1:12151,2\n1645#1:12157\n1661#1:12170,6\n1661#1:12185,4\n1661#1:12195,2\n1661#1:12201\n1623#1:12205\n1679#1:12215,6\n1679#1:12230,4\n1679#1:12240,2\n1679#1:12248\n1617#1:12252\n1699#1:12262,6\n1699#1:12277,4\n1699#1:12287,2\n1700#1:12299,6\n1700#1:12314,4\n1700#1:12324,2\n1700#1:12332\n1716#1:12343,6\n1716#1:12358,4\n1716#1:12368,2\n1716#1:12376\n1699#1:12380\n1736#1:12393,6\n1736#1:12408,4\n1736#1:12418,2\n1745#1:12430,6\n1745#1:12445,4\n1745#1:12455,2\n1745#1:12462\n1736#1:12466\n1772#1:12480,6\n1772#1:12495,4\n1772#1:12505,2\n1780#1:12516,6\n1780#1:12531,4\n1780#1:12541,2\n1781#1:12548,6\n1781#1:12563,4\n1781#1:12573,2\n1786#1:12585,6\n1786#1:12600,4\n1786#1:12610,2\n1786#1:12616\n1801#1:12626,6\n1801#1:12641,4\n1801#1:12651,2\n1801#1:12657\n1781#1:12661\n1821#1:12673,6\n1821#1:12688,4\n1821#1:12698,2\n1821#1:12705\n1843#1:12711,6\n1843#1:12726,4\n1843#1:12736,2\n1843#1:12742\n1780#1:12746\n1772#1:12750\n1867#1:12769,6\n1867#1:12784,4\n1867#1:12794,2\n1876#1:12801,6\n1876#1:12816,4\n1876#1:12826,2\n1881#1:12838,6\n1881#1:12853,4\n1881#1:12863,2\n1881#1:12870\n1876#1:12874\n1867#1:12878\n1903#1:12891,6\n1903#1:12906,4\n1903#1:12916,2\n1903#1:12922\n1923#1:12929,6\n1923#1:12944,4\n1923#1:12954,2\n1929#1:12973,6\n1929#1:12988,4\n1929#1:12998,2\n1943#1:13010,6\n1943#1:13025,4\n1943#1:13035,2\n1943#1:13043\n1929#1:13047\n1964#1:13063,6\n1964#1:13078,4\n1964#1:13088,2\n1964#1:13095\n1982#1:13111,6\n1982#1:13126,4\n1982#1:13136,2\n1982#1:13143\n2001#1:13159,6\n2001#1:13174,4\n2001#1:13184,2\n2001#1:13191\n1923#1:13195\n1611#1:13199\n2377#1:13295,6\n2377#1:13310,4\n2377#1:13320,2\n2377#1:13326\n2406#1:13335,6\n2406#1:13350,4\n2406#1:13360,2\n2406#1:13367\n2459#1:13377,6\n2459#1:13392,4\n2459#1:13402,2\n2489#1:13424,6\n2489#1:13439,4\n2489#1:13449,2\n2507#1:13461,6\n2507#1:13476,4\n2507#1:13486,2\n2507#1:13494\n2489#1:13498\n2534#1:13504,6\n2534#1:13519,4\n2534#1:13529,2\n2540#1:13543,6\n2540#1:13558,4\n2540#1:13568,2\n2540#1:13574\n2556#1:13592,6\n2556#1:13607,4\n2556#1:13617,2\n2556#1:13626\n2534#1:13630\n2586#1:13636,6\n2586#1:13651,4\n2586#1:13661,2\n2592#1:13675,6\n2592#1:13690,4\n2592#1:13700,2\n2599#1:13712,6\n2599#1:13727,4\n2599#1:13737,2\n2607#1:13751,6\n2607#1:13766,4\n2607#1:13776,2\n2607#1:13782\n2599#1:13786\n2592#1:13790\n2624#1:13800,6\n2624#1:13815,4\n2624#1:13825,2\n2624#1:13844\n2586#1:13848\n2459#1:13852\n2924#1:13863,6\n2924#1:13878,4\n2924#1:13888,2\n2930#1:13895,6\n2930#1:13910,4\n2930#1:13920,2\n2935#1:13932,6\n2935#1:13947,4\n2935#1:13957,2\n2935#1:13965\n2951#1:13977,6\n2951#1:13992,4\n2951#1:14002,2\n2951#1:14008\n2930#1:14012\n2979#1:14019,6\n2979#1:14034,4\n2979#1:14044,2\n2987#1:14061,6\n2987#1:14076,4\n2987#1:14086,2\n2987#1:14092\n3001#1:14107,6\n3001#1:14122,4\n3001#1:14132,2\n3001#1:14138\n3015#1:14153,6\n3015#1:14168,4\n3015#1:14178,2\n3015#1:14184\n3029#1:14199,6\n3029#1:14214,4\n3029#1:14224,2\n3029#1:14230\n3043#1:14245,6\n3043#1:14260,4\n3043#1:14270,2\n3043#1:14276\n3061#1:14292,6\n3061#1:14307,4\n3061#1:14317,2\n3061#1:14323\n2979#1:14327\n3088#1:14344,6\n3088#1:14359,4\n3088#1:14369,2\n3101#1:14381,6\n3101#1:14396,4\n3101#1:14406,2\n3101#1:14414\n3088#1:14418\n2924#1:14422\n3293#1:14432,6\n3293#1:14447,4\n3293#1:14457,2\n3301#1:14468,6\n3301#1:14483,4\n3301#1:14493,2\n3301#1:14499\n3344#1:14513,6\n3344#1:14528,4\n3344#1:14538,2\n3352#1:14549,6\n3352#1:14564,4\n3352#1:14574,2\n3353#1:14581,6\n3353#1:14596,4\n3353#1:14606,2\n3368#1:14619,6\n3368#1:14634,4\n3368#1:14644,2\n3368#1:14650\n3382#1:14661,6\n3382#1:14676,4\n3382#1:14686,2\n3382#1:14692\n3353#1:14696\n3411#1:14706,6\n3411#1:14721,4\n3411#1:14731,2\n3411#1:14737\n3352#1:14742\n3344#1:14746\n3451#1:14760,6\n3451#1:14775,4\n3451#1:14785,2\n3459#1:14792,6\n3459#1:14807,4\n3459#1:14817,2\n3464#1:14828,6\n3464#1:14843,4\n3464#1:14853,2\n3464#1:14859\n3459#1:14871\n3451#1:14875\n3500#1:14889,6\n3500#1:14904,4\n3500#1:14914,2\n3508#1:14925,6\n3508#1:14940,4\n3508#1:14950,2\n3509#1:14957,6\n3509#1:14972,4\n3509#1:14982,2\n3514#1:14993,6\n3514#1:15008,4\n3514#1:15018,2\n3514#1:15024\n3509#1:15036\n3508#1:15041\n3500#1:15045\n3571#1:15056,6\n3571#1:15071,4\n3571#1:15081,2\n3571#1:15105\n3613#1:15115,6\n3613#1:15130,4\n3613#1:15140,2\n3613#1:15164\n3653#1:15176,6\n3653#1:15191,4\n3653#1:15201,2\n3660#1:15213,6\n3660#1:15228,4\n3660#1:15238,2\n3660#1:15245\n3653#1:15249\n3293#1:15256\n3882#1:15304,6\n3882#1:15319,4\n3882#1:15329,2\n3882#1:15336\n3902#1:15345,6\n3902#1:15360,4\n3902#1:15370,2\n3947#1:15398,6\n3947#1:15413,4\n3947#1:15423,2\n3955#1:15435,6\n3955#1:15450,4\n3955#1:15460,2\n3955#1:15467\n3947#1:15471\n3988#1:15490,6\n3988#1:15505,4\n3988#1:15515,2\n3996#1:15528,6\n3996#1:15543,4\n3996#1:15553,2\n3997#1:15560,6\n3997#1:15575,4\n3997#1:15585,2\n4003#1:15598,6\n4003#1:15613,4\n4003#1:15623,2\n4003#1:15629\n3997#1:15633\n3996#1:15637\n3988#1:15641\n4022#1:15657,6\n4022#1:15672,4\n4022#1:15682,2\n4022#1:15695\n4058#1:15715,6\n4058#1:15730,4\n4058#1:15740,2\n4073#1:15752,6\n4073#1:15767,4\n4073#1:15777,2\n4074#1:15789,6\n4074#1:15804,4\n4074#1:15814,2\n4074#1:15827\n4073#1:15833\n4058#1:15837\n3902#1:15841\n4269#1:15859,6\n4269#1:15874,4\n4269#1:15884,2\n4271#1:15897,6\n4271#1:15912,4\n4271#1:15922,2\n4271#1:15938\n4269#1:15942\n4316#1:15961,6\n4316#1:15976,4\n4316#1:15986,2\n4316#1:15999\n4410#1:16039,6\n4410#1:16054,4\n4410#1:16064,2\n4416#1:16071,6\n4416#1:16086,4\n4416#1:16096,2\n4416#1:16102\n4410#1:16135\n4769#1:16193,6\n4769#1:16208,4\n4769#1:16218,2\n4769#1:16225\n4839#1:16247,6\n4839#1:16262,4\n4839#1:16272,2\n4845#1:16283,6\n4845#1:16298,4\n4845#1:16308,2\n4849#1:16322,6\n4849#1:16337,4\n4849#1:16347,2\n4849#1:16353\n4860#1:16364,6\n4860#1:16379,4\n4860#1:16389,2\n4860#1:16395\n4845#1:16399\n4878#1:16419,6\n4878#1:16434,4\n4878#1:16444,2\n4891#1:16451,6\n4891#1:16466,4\n4891#1:16476,2\n4896#1:16488,6\n4896#1:16503,4\n4896#1:16513,2\n4897#1:16525,6\n4897#1:16540,4\n4897#1:16550,2\n4897#1:16556\n4907#1:16567,6\n4907#1:16582,4\n4907#1:16592,2\n4907#1:16598\n4896#1:16602\n4891#1:16606\n4878#1:16610\n4957#1:16631,6\n4957#1:16646,4\n4957#1:16656,2\n4971#1:16668,6\n4971#1:16683,4\n4971#1:16693,2\n4975#1:16704,6\n4975#1:16719,4\n4975#1:16729,2\n4975#1:16735\n4971#1:16741\n4999#1:16751,6\n4999#1:16766,4\n4999#1:16776,2\n5022#1:16792,6\n5022#1:16807,4\n5022#1:16817,2\n5022#1:16823\n4999#1:16827\n4957#1:16831\n4839#1:16835\n5057#1:16851,6\n5057#1:16866,4\n5057#1:16876,2\n5063#1:16887,6\n5063#1:16902,4\n5063#1:16912,2\n5067#1:16926,6\n5067#1:16941,4\n5067#1:16951,2\n5067#1:16957\n5078#1:16968,6\n5078#1:16983,4\n5078#1:16993,2\n5078#1:16999\n5063#1:17003\n5119#1:17022,6\n5119#1:17037,4\n5119#1:17047,2\n5134#1:17068,6\n5134#1:17083,4\n5134#1:17093,2\n5134#1:17099\n5119#1:17104\n5199#1:17130,6\n5199#1:17145,4\n5199#1:17155,2\n5206#1:17175,6\n5206#1:17190,4\n5206#1:17200,2\n5206#1:17206\n5229#1:17224,6\n5229#1:17239,4\n5229#1:17249,2\n5229#1:17255\n5199#1:17259\n5057#1:17263\n5390#1:17300,6\n5390#1:17315,4\n5390#1:17325,2\n5398#1:17332,6\n5398#1:17347,4\n5398#1:17357,2\n5403#1:17368,6\n5403#1:17383,4\n5403#1:17393,2\n5409#1:17406,6\n5409#1:17421,4\n5409#1:17431,2\n5409#1:17438\n5403#1:17442\n5426#1:17452,6\n5426#1:17467,4\n5426#1:17477,2\n5426#1:17484\n5398#1:17488\n5444#1:17497,6\n5444#1:17512,4\n5444#1:17522,2\n5449#1:17533,6\n5449#1:17548,4\n5449#1:17558,2\n5450#1:17570,6\n5450#1:17585,4\n5450#1:17595,2\n5450#1:17601\n5465#1:17614,6\n5465#1:17629,4\n5465#1:17639,2\n5465#1:17651\n5449#1:17655\n5501#1:17664,6\n5501#1:17679,4\n5501#1:17689,2\n5509#1:17704,6\n5509#1:17719,4\n5509#1:17729,2\n5509#1:17741\n5501#1:17745\n5444#1:17749\n5546#1:17758,6\n5546#1:17773,4\n5546#1:17783,2\n5546#1:17790\n5561#1:17799,6\n5561#1:17814,4\n5561#1:17824,2\n5569#1:17845,6\n5569#1:17860,4\n5569#1:17870,2\n5580#1:17877,6\n5580#1:17892,4\n5580#1:17902,2\n5580#1:17909\n5569#1:17913\n5561#1:17917\n5602#1:17926,6\n5602#1:17941,4\n5602#1:17951,2\n5610#1:17966,6\n5610#1:17981,4\n5610#1:17991,2\n5620#1:17998,6\n5620#1:18013,4\n5620#1:18023,2\n5620#1:18042\n5610#1:18046\n5602#1:18050\n5665#1:18059,6\n5665#1:18074,4\n5665#1:18084,2\n5673#1:18099,6\n5673#1:18114,4\n5673#1:18124,2\n5683#1:18131,6\n5683#1:18146,4\n5683#1:18156,2\n5683#1:18175\n5673#1:18179\n5665#1:18183\n5728#1:18195,6\n5728#1:18210,4\n5728#1:18220,2\n5736#1:18231,6\n5736#1:18246,4\n5736#1:18256,2\n5750#1:18276,6\n5750#1:18291,4\n5750#1:18301,2\n5750#1:18308\n5736#1:18312\n5728#1:18316\n5390#1:18328\n5872#1:18356,6\n5872#1:18371,4\n5872#1:18381,2\n5877#1:18388,6\n5877#1:18403,4\n5877#1:18413,2\n5877#1:18420\n5889#1:18432,6\n5889#1:18447,4\n5889#1:18457,2\n5889#1:18469\n5872#1:18480\n5959#1:18492,6\n5959#1:18507,4\n5959#1:18517,2\n5966#1:18529,6\n5966#1:18544,4\n5966#1:18554,2\n5966#1:18560\n6016#1:18576,6\n6016#1:18591,4\n6016#1:18601,2\n6024#1:18614,6\n6024#1:18629,4\n6024#1:18639,2\n6025#1:18651,6\n6025#1:18666,4\n6025#1:18676,2\n6025#1:18683\n6035#1:18693,6\n6035#1:18708,4\n6035#1:18718,2\n6035#1:18725\n6045#1:18735,6\n6045#1:18750,4\n6045#1:18760,2\n6045#1:18767\n6024#1:18771\n6016#1:18775\n6061#1:18786,6\n6061#1:18801,4\n6061#1:18811,2\n6074#1:18823,6\n6074#1:18838,4\n6074#1:18848,2\n6074#1:18855\n6061#1:18859\n5959#1:18864\n5943#1:18873,6\n5943#1:18888,4\n5943#1:18898,2\n5943#1:18908\n6137#1:18918,6\n6137#1:18933,4\n6137#1:18943,2\n6144#1:18955,6\n6144#1:18970,4\n6144#1:18980,2\n6144#1:18986\n6194#1:19002,6\n6194#1:19017,4\n6194#1:19027,2\n6202#1:19040,6\n6202#1:19055,4\n6202#1:19065,2\n6203#1:19077,6\n6203#1:19092,4\n6203#1:19102,2\n6203#1:19109\n6213#1:19119,6\n6213#1:19134,4\n6213#1:19144,2\n6213#1:19151\n6223#1:19161,6\n6223#1:19176,4\n6223#1:19186,2\n6223#1:19193\n6202#1:19197\n6194#1:19201\n6239#1:19211,6\n6239#1:19226,4\n6239#1:19236,2\n6239#1:19246\n6137#1:19250\n6121#1:19259,6\n6121#1:19274,4\n6121#1:19284,2\n6121#1:19294\n6600#1:19313,6\n6600#1:19328,4\n6600#1:19338,2\n6608#1:19345,6\n6608#1:19360,4\n6608#1:19370,2\n6613#1:19381,6\n6613#1:19396,4\n6613#1:19406,2\n6618#1:19417,6\n6618#1:19432,4\n6618#1:19442,2\n6618#1:19448\n6640#1:19458,6\n6640#1:19473,4\n6640#1:19483,2\n6640#1:19489\n6657#1:19500,6\n6657#1:19515,4\n6657#1:19525,2\n6658#1:19537,6\n6658#1:19552,4\n6658#1:19562,2\n6669#1:19586,6\n6669#1:19601,4\n6669#1:19611,2\n6669#1:19617\n6684#1:19641,6\n6684#1:19656,4\n6684#1:19666,2\n6684#1:19672\n6658#1:19676\n6657#1:19681\n6613#1:19685\n6713#1:19695,6\n6713#1:19710,4\n6713#1:19720,2\n6713#1:19726\n6608#1:19730\n6600#1:19734\n6334#1:19743,6\n6334#1:19758,4\n6334#1:19768,2\n6338#1:19780,6\n6338#1:19795,4\n6338#1:19805,2\n6348#1:19812,6\n6348#1:19827,4\n6348#1:19837,2\n6353#1:19848,6\n6353#1:19863,4\n6353#1:19873,2\n6357#1:19885,6\n6357#1:19900,4\n6357#1:19910,2\n6357#1:19916\n6368#1:19927,6\n6368#1:19942,4\n6368#1:19952,2\n6368#1:19958\n6353#1:19962\n6383#1:19971,6\n6383#1:19986,4\n6383#1:19996,2\n6383#1:20003\n6348#1:20007\n6338#1:20011\n6403#1:20024,6\n6403#1:20039,4\n6403#1:20049,2\n6412#1:20060,6\n6412#1:20075,4\n6412#1:20085,2\n6413#1:20092,6\n6413#1:20107,4\n6413#1:20117,2\n6418#1:20128,6\n6418#1:20143,4\n6418#1:20153,2\n6418#1:20160\n6439#1:20172,6\n6439#1:20187,4\n6439#1:20197,2\n6439#1:20203\n6413#1:20207\n6457#1:20213,6\n6457#1:20228,4\n6457#1:20238,2\n6457#1:20244\n6478#1:20256,6\n6478#1:20271,4\n6478#1:20281,2\n6478#1:20288\n6412#1:20292\n6403#1:20296\n6502#1:20306,6\n6502#1:20321,4\n6502#1:20331,2\n6502#1:20337\n6518#1:20345,6\n6518#1:20360,4\n6518#1:20370,2\n6524#1:20382,6\n6524#1:20397,4\n6524#1:20407,2\n6524#1:20416\n6518#1:20420\n6539#1:20429,6\n6539#1:20444,4\n6539#1:20454,2\n6546#1:20467,6\n6546#1:20482,4\n6546#1:20492,2\n6555#1:20504,6\n6555#1:20519,4\n6555#1:20529,2\n6555#1:20537\n6546#1:20541\n6539#1:20545\n6734#1:20563,6\n6734#1:20578,4\n6734#1:20588,2\n6739#1:20600,6\n6739#1:20615,4\n6739#1:20625,2\n6739#1:20631\n6734#1:20635\n6334#1:20639\n6318#1:20648,6\n6318#1:20663,4\n6318#1:20673,2\n6318#1:20682\n6782#1:20692,6\n6782#1:20707,4\n6782#1:20717,2\n6787#1:20724,6\n6787#1:20739,4\n6787#1:20749,2\n6787#1:20756\n6782#1:20765\n417#1:7145,9\n417#1:7166\n423#1:7181,9\n423#1:7202\n423#1:7232,2\n417#1:7380,2\n2271#1:7706,9\n2271#1:7727\n2276#1:7742,9\n2276#1:7763\n2284#1:7779,9\n2284#1:7800\n2290#1:7815,9\n2290#1:7836\n2329#1:7861,9\n2329#1:7882\n2335#1:7898,9\n2335#1:7919\n2335#1:7923,2\n2329#1:7927,2\n2290#1:7955,2\n2284#1:7959,2\n2648#1:7996,9\n2648#1:8017\n2656#1:8034,9\n2656#1:8055\n2665#1:8077,9\n2665#1:8098\n2665#1:8107,2\n2691#1:8133,9\n2691#1:8154\n2699#1:8170,9\n2699#1:8191\n2699#1:8194,2\n2691#1:8198,2\n2656#1:8202,2\n2714#1:8219,9\n2714#1:8240\n2714#1:8248,2\n2728#1:8266,9\n2728#1:8287\n2746#1:8310,9\n2746#1:8331\n2746#1:8334,2\n2728#1:8357,2\n2648#1:8361,2\n2788#1:8395,9\n2788#1:8416\n2799#1:8427,9\n2799#1:8448\n2804#1:8464,9\n2804#1:8485\n2807#1:8502,9\n2807#1:8523\n2807#1:8525,2\n2804#1:8529,2\n2821#1:8547,9\n2821#1:8568\n2821#1:8572,2\n2799#1:8576,2\n2788#1:8580,2\n2842#1:8605,9\n2842#1:8626\n2848#1:8650,9\n2848#1:8671\n2863#1:8682,9\n2863#1:8703\n2868#1:8719,9\n2868#1:8740\n2868#1:8742,2\n2882#1:8760,9\n2882#1:8781\n2882#1:8785,2\n2863#1:8789,2\n2848#1:8793,2\n2842#1:8797,2\n2276#1:8810,2\n2271#1:8904,2\n3716#1:8938,9\n3716#1:8959\n3728#1:8975,9\n3728#1:8996\n3728#1:8999,2\n3716#1:9003,2\n3791#1:9032,9\n3791#1:9053\n3799#1:9070,9\n3799#1:9091\n3799#1:9095,2\n3791#1:9099,2\n4667#1:9329,9\n4667#1:9350\n4672#1:9365,9\n4672#1:9386\n4680#1:9403,9\n4680#1:9424\n4691#1:9439,9\n4691#1:9460\n4717#1:9478,9\n4717#1:9499\n4717#1:9502,2\n4736#1:9520,9\n4736#1:9541\n4736#1:9543,2\n4691#1:9553,2\n4680#1:9557,2\n5257#1:9617,9\n5257#1:9638\n5263#1:9662,9\n5263#1:9683\n5281#1:9699,9\n5281#1:9720\n5281#1:9724,2\n5263#1:9728,2\n5257#1:9732,2\n4672#1:9737,2\n4667#1:9771,2\n441#1:10032,9\n441#1:10053\n449#1:10069,9\n449#1:10090\n449#1:10093,2\n467#1:10110,9\n467#1:10131\n480#1:10160,9\n480#1:10181\n480#1:10183,2\n467#1:10187,2\n497#1:10206,9\n497#1:10227\n502#1:10249,9\n502#1:10270\n502#1:10272,2\n521#1:10296,9\n521#1:10317\n521#1:10320,2\n539#1:10344,9\n539#1:10365\n539#1:10367,2\n497#1:10371,2\n441#1:10375,2\n592#1:10398,9\n592#1:10419\n600#1:10444,9\n600#1:10465\n613#1:10481,9\n613#1:10502\n624#1:10520,9\n624#1:10541\n624#1:10543,2\n613#1:10547,2\n600#1:10551,2\n648#1:10578,9\n648#1:10599\n661#1:10615,9\n661#1:10636\n672#1:10654,9\n672#1:10675\n672#1:10677,2\n661#1:10681,2\n648#1:10685,2\n592#1:10689,2\n747#1:10740,9\n747#1:10761\n759#1:10777,9\n759#1:10798\n759#1:10801,2\n747#1:10805,2\n777#1:10841,9\n777#1:10862\n788#1:10887,9\n788#1:10908\n802#1:10923,9\n802#1:10944\n812#1:10962,9\n812#1:10983\n813#1:10999,9\n813#1:11020\n813#1:11023,2\n812#1:11028,2\n834#1:11047,9\n834#1:11068\n834#1:11071,2\n802#1:11075,2\n788#1:11079,2\n852#1:11106,9\n852#1:11127\n866#1:11142,9\n866#1:11163\n876#1:11181,9\n876#1:11202\n876#1:11205,2\n894#1:11224,9\n894#1:11245\n894#1:11248,2\n866#1:11252,2\n852#1:11256,2\n777#1:11260,2\n975#1:11296,9\n975#1:11317\n975#1:11320,2\n1340#1:11371,9\n1340#1:11392\n1346#1:11403,9\n1346#1:11424\n1351#1:11441,9\n1351#1:11462\n1356#1:11480,9\n1356#1:11501\n1356#1:11503,2\n1369#1:11523,9\n1369#1:11544\n1369#1:11546,2\n1351#1:11550,2\n1399#1:11570,9\n1399#1:11591\n1399#1:11593,2\n1346#1:11597,2\n1418#1:11615,9\n1418#1:11636\n1418#1:11641,2\n1470#1:11662,9\n1470#1:11683\n1478#1:11699,9\n1478#1:11720\n1479#1:11734,9\n1479#1:11755\n1479#1:11757,2\n1478#1:11770,2\n1470#1:11774,2\n1512#1:11788,9\n1512#1:11809\n1517#1:11832,9\n1517#1:11853\n1517#1:11858,2\n1534#1:11885,9\n1534#1:11906\n1534#1:11908,2\n1512#1:11912,2\n1340#1:11920,2\n1611#1:11938,9\n1611#1:11959\n1617#1:11970,9\n1617#1:11991\n1623#1:12007,9\n1623#1:12028\n1624#1:12046,9\n1624#1:12067\n1631#1:12083,9\n1631#1:12104\n1631#1:12107,2\n1624#1:12111,2\n1645#1:12132,9\n1645#1:12153\n1645#1:12155,2\n1661#1:12176,9\n1661#1:12197\n1661#1:12199,2\n1623#1:12203,2\n1679#1:12221,9\n1679#1:12242\n1679#1:12246,2\n1617#1:12250,2\n1699#1:12268,9\n1699#1:12289\n1700#1:12305,9\n1700#1:12326\n1700#1:12330,2\n1716#1:12349,9\n1716#1:12370\n1716#1:12374,2\n1699#1:12378,2\n1736#1:12399,9\n1736#1:12420\n1745#1:12436,9\n1745#1:12457\n1745#1:12460,2\n1736#1:12464,2\n1772#1:12486,9\n1772#1:12507\n1780#1:12522,9\n1780#1:12543\n1781#1:12554,9\n1781#1:12575\n1786#1:12591,9\n1786#1:12612\n1786#1:12614,2\n1801#1:12632,9\n1801#1:12653\n1801#1:12655,2\n1781#1:12659,2\n1821#1:12679,9\n1821#1:12700\n1821#1:12703,2\n1843#1:12717,9\n1843#1:12738\n1843#1:12740,2\n1780#1:12744,2\n1772#1:12748,2\n1867#1:12775,9\n1867#1:12796\n1876#1:12807,9\n1876#1:12828\n1881#1:12844,9\n1881#1:12865\n1881#1:12868,2\n1876#1:12872,2\n1867#1:12876,2\n1903#1:12897,9\n1903#1:12918\n1903#1:12920,2\n1923#1:12935,9\n1923#1:12956\n1929#1:12979,9\n1929#1:13000\n1943#1:13016,9\n1943#1:13037\n1943#1:13041,2\n1929#1:13045,2\n1964#1:13069,9\n1964#1:13090\n1964#1:13093,2\n1982#1:13117,9\n1982#1:13138\n1982#1:13141,2\n2001#1:13165,9\n2001#1:13186\n2001#1:13189,2\n1923#1:13193,2\n1611#1:13197,2\n2377#1:13301,9\n2377#1:13322\n2377#1:13324,2\n2406#1:13341,9\n2406#1:13362\n2406#1:13365,2\n2459#1:13383,9\n2459#1:13404\n2489#1:13430,9\n2489#1:13451\n2507#1:13467,9\n2507#1:13488\n2507#1:13492,2\n2489#1:13496,2\n2534#1:13510,9\n2534#1:13531\n2540#1:13549,9\n2540#1:13570\n2540#1:13572,2\n2556#1:13598,9\n2556#1:13619\n2556#1:13624,2\n2534#1:13628,2\n2586#1:13642,9\n2586#1:13663\n2592#1:13681,9\n2592#1:13702\n2599#1:13718,9\n2599#1:13739\n2607#1:13757,9\n2607#1:13778\n2607#1:13780,2\n2599#1:13784,2\n2592#1:13788,2\n2624#1:13806,9\n2624#1:13827\n2624#1:13842,2\n2586#1:13846,2\n2459#1:13850,2\n2924#1:13869,9\n2924#1:13890\n2930#1:13901,9\n2930#1:13922\n2935#1:13938,9\n2935#1:13959\n2935#1:13963,2\n2951#1:13983,9\n2951#1:14004\n2951#1:14006,2\n2930#1:14010,2\n2979#1:14025,9\n2979#1:14046\n2987#1:14067,9\n2987#1:14088\n2987#1:14090,2\n3001#1:14113,9\n3001#1:14134\n3001#1:14136,2\n3015#1:14159,9\n3015#1:14180\n3015#1:14182,2\n3029#1:14205,9\n3029#1:14226\n3029#1:14228,2\n3043#1:14251,9\n3043#1:14272\n3043#1:14274,2\n3061#1:14298,9\n3061#1:14319\n3061#1:14321,2\n2979#1:14325,2\n3088#1:14350,9\n3088#1:14371\n3101#1:14387,9\n3101#1:14408\n3101#1:14412,2\n3088#1:14416,2\n2924#1:14420,2\n3293#1:14438,9\n3293#1:14459\n3301#1:14474,9\n3301#1:14495\n3301#1:14497,2\n3344#1:14519,9\n3344#1:14540\n3352#1:14555,9\n3352#1:14576\n3353#1:14587,9\n3353#1:14608\n3368#1:14625,9\n3368#1:14646\n3368#1:14648,2\n3382#1:14667,9\n3382#1:14688\n3382#1:14690,2\n3353#1:14694,2\n3411#1:14712,9\n3411#1:14733\n3411#1:14735,2\n3352#1:14740,2\n3344#1:14744,2\n3451#1:14766,9\n3451#1:14787\n3459#1:14798,9\n3459#1:14819\n3464#1:14834,9\n3464#1:14855\n3464#1:14857,2\n3459#1:14869,2\n3451#1:14873,2\n3500#1:14895,9\n3500#1:14916\n3508#1:14931,9\n3508#1:14952\n3509#1:14963,9\n3509#1:14984\n3514#1:14999,9\n3514#1:15020\n3514#1:15022,2\n3509#1:15034,2\n3508#1:15039,2\n3500#1:15043,2\n3571#1:15062,9\n3571#1:15083\n3571#1:15103,2\n3613#1:15121,9\n3613#1:15142\n3613#1:15162,2\n3653#1:15182,9\n3653#1:15203\n3660#1:15219,9\n3660#1:15240\n3660#1:15243,2\n3653#1:15247,2\n3293#1:15254,2\n3882#1:15310,9\n3882#1:15331\n3882#1:15334,2\n3902#1:15351,9\n3902#1:15372\n3947#1:15404,9\n3947#1:15425\n3955#1:15441,9\n3955#1:15462\n3955#1:15465,2\n3947#1:15469,2\n3988#1:15496,9\n3988#1:15517\n3996#1:15534,9\n3996#1:15555\n3997#1:15566,9\n3997#1:15587\n4003#1:15604,9\n4003#1:15625\n4003#1:15627,2\n3997#1:15631,2\n3996#1:15635,2\n3988#1:15639,2\n4022#1:15663,9\n4022#1:15684\n4022#1:15693,2\n4058#1:15721,9\n4058#1:15742\n4073#1:15758,9\n4073#1:15779\n4074#1:15795,9\n4074#1:15816\n4074#1:15825,2\n4073#1:15831,2\n4058#1:15835,2\n3902#1:15839,2\n4269#1:15865,9\n4269#1:15886\n4271#1:15903,9\n4271#1:15924\n4271#1:15936,2\n4269#1:15940,2\n4316#1:15967,9\n4316#1:15988\n4316#1:15997,2\n4410#1:16045,9\n4410#1:16066\n4416#1:16077,9\n4416#1:16098\n4416#1:16100,2\n4410#1:16133,2\n4769#1:16199,9\n4769#1:16220\n4769#1:16223,2\n4839#1:16253,9\n4839#1:16274\n4845#1:16289,9\n4845#1:16310\n4849#1:16328,9\n4849#1:16349\n4849#1:16351,2\n4860#1:16370,9\n4860#1:16391\n4860#1:16393,2\n4845#1:16397,2\n4878#1:16425,9\n4878#1:16446\n4891#1:16457,9\n4891#1:16478\n4896#1:16494,9\n4896#1:16515\n4897#1:16531,9\n4897#1:16552\n4897#1:16554,2\n4907#1:16573,9\n4907#1:16594\n4907#1:16596,2\n4896#1:16600,2\n4891#1:16604,2\n4878#1:16608,2\n4957#1:16637,9\n4957#1:16658\n4971#1:16674,9\n4971#1:16695\n4975#1:16710,9\n4975#1:16731\n4975#1:16733,2\n4971#1:16739,2\n4999#1:16757,9\n4999#1:16778\n5022#1:16798,9\n5022#1:16819\n5022#1:16821,2\n4999#1:16825,2\n4957#1:16829,2\n4839#1:16833,2\n5057#1:16857,9\n5057#1:16878\n5063#1:16893,9\n5063#1:16914\n5067#1:16932,9\n5067#1:16953\n5067#1:16955,2\n5078#1:16974,9\n5078#1:16995\n5078#1:16997,2\n5063#1:17001,2\n5119#1:17028,9\n5119#1:17049\n5134#1:17074,9\n5134#1:17095\n5134#1:17097,2\n5119#1:17102,2\n5199#1:17136,9\n5199#1:17157\n5206#1:17181,9\n5206#1:17202\n5206#1:17204,2\n5229#1:17230,9\n5229#1:17251\n5229#1:17253,2\n5199#1:17257,2\n5057#1:17261,2\n5390#1:17306,9\n5390#1:17327\n5398#1:17338,9\n5398#1:17359\n5403#1:17374,9\n5403#1:17395\n5409#1:17412,9\n5409#1:17433\n5409#1:17436,2\n5403#1:17440,2\n5426#1:17458,9\n5426#1:17479\n5426#1:17482,2\n5398#1:17486,2\n5444#1:17503,9\n5444#1:17524\n5449#1:17539,9\n5449#1:17560\n5450#1:17576,9\n5450#1:17597\n5450#1:17599,2\n5465#1:17620,9\n5465#1:17641\n5465#1:17649,2\n5449#1:17653,2\n5501#1:17670,9\n5501#1:17691\n5509#1:17710,9\n5509#1:17731\n5509#1:17739,2\n5501#1:17743,2\n5444#1:17747,2\n5546#1:17764,9\n5546#1:17785\n5546#1:17788,2\n5561#1:17805,9\n5561#1:17826\n5569#1:17851,9\n5569#1:17872\n5580#1:17883,9\n5580#1:17904\n5580#1:17907,2\n5569#1:17911,2\n5561#1:17915,2\n5602#1:17932,9\n5602#1:17953\n5610#1:17972,9\n5610#1:17993\n5620#1:18004,9\n5620#1:18025\n5620#1:18040,2\n5610#1:18044,2\n5602#1:18048,2\n5665#1:18065,9\n5665#1:18086\n5673#1:18105,9\n5673#1:18126\n5683#1:18137,9\n5683#1:18158\n5683#1:18173,2\n5673#1:18177,2\n5665#1:18181,2\n5728#1:18201,9\n5728#1:18222\n5736#1:18237,9\n5736#1:18258\n5750#1:18282,9\n5750#1:18303\n5750#1:18306,2\n5736#1:18310,2\n5728#1:18314,2\n5390#1:18326,2\n5872#1:18362,9\n5872#1:18383\n5877#1:18394,9\n5877#1:18415\n5877#1:18418,2\n5889#1:18438,9\n5889#1:18459\n5889#1:18467,2\n5872#1:18478,2\n5959#1:18498,9\n5959#1:18519\n5966#1:18535,9\n5966#1:18556\n5966#1:18558,2\n6016#1:18582,9\n6016#1:18603\n6024#1:18620,9\n6024#1:18641\n6025#1:18657,9\n6025#1:18678\n6025#1:18681,2\n6035#1:18699,9\n6035#1:18720\n6035#1:18723,2\n6045#1:18741,9\n6045#1:18762\n6045#1:18765,2\n6024#1:18769,2\n6016#1:18773,2\n6061#1:18792,9\n6061#1:18813\n6074#1:18829,9\n6074#1:18850\n6074#1:18853,2\n6061#1:18857,2\n5959#1:18862,2\n5943#1:18879,9\n5943#1:18900\n5943#1:18906,2\n6137#1:18924,9\n6137#1:18945\n6144#1:18961,9\n6144#1:18982\n6144#1:18984,2\n6194#1:19008,9\n6194#1:19029\n6202#1:19046,9\n6202#1:19067\n6203#1:19083,9\n6203#1:19104\n6203#1:19107,2\n6213#1:19125,9\n6213#1:19146\n6213#1:19149,2\n6223#1:19167,9\n6223#1:19188\n6223#1:19191,2\n6202#1:19195,2\n6194#1:19199,2\n6239#1:19217,9\n6239#1:19238\n6239#1:19244,2\n6137#1:19248,2\n6121#1:19265,9\n6121#1:19286\n6121#1:19292,2\n6600#1:19319,9\n6600#1:19340\n6608#1:19351,9\n6608#1:19372\n6613#1:19387,9\n6613#1:19408\n6618#1:19423,9\n6618#1:19444\n6618#1:19446,2\n6640#1:19464,9\n6640#1:19485\n6640#1:19487,2\n6657#1:19506,9\n6657#1:19527\n6658#1:19543,9\n6658#1:19564\n6669#1:19592,9\n6669#1:19613\n6669#1:19615,2\n6684#1:19647,9\n6684#1:19668\n6684#1:19670,2\n6658#1:19674,2\n6657#1:19679,2\n6613#1:19683,2\n6713#1:19701,9\n6713#1:19722\n6713#1:19724,2\n6608#1:19728,2\n6600#1:19732,2\n6334#1:19749,9\n6334#1:19770\n6338#1:19786,9\n6338#1:19807\n6348#1:19818,9\n6348#1:19839\n6353#1:19854,9\n6353#1:19875\n6357#1:19891,9\n6357#1:19912\n6357#1:19914,2\n6368#1:19933,9\n6368#1:19954\n6368#1:19956,2\n6353#1:19960,2\n6383#1:19977,9\n6383#1:19998\n6383#1:20001,2\n6348#1:20005,2\n6338#1:20009,2\n6403#1:20030,9\n6403#1:20051\n6412#1:20066,9\n6412#1:20087\n6413#1:20098,9\n6413#1:20119\n6418#1:20134,9\n6418#1:20155\n6418#1:20158,2\n6439#1:20178,9\n6439#1:20199\n6439#1:20201,2\n6413#1:20205,2\n6457#1:20219,9\n6457#1:20240\n6457#1:20242,2\n6478#1:20262,9\n6478#1:20283\n6478#1:20286,2\n6412#1:20290,2\n6403#1:20294,2\n6502#1:20312,9\n6502#1:20333\n6502#1:20335,2\n6518#1:20351,9\n6518#1:20372\n6524#1:20388,9\n6524#1:20409\n6524#1:20414,2\n6518#1:20418,2\n6539#1:20435,9\n6539#1:20456\n6546#1:20473,9\n6546#1:20494\n6555#1:20510,9\n6555#1:20531\n6555#1:20535,2\n6546#1:20539,2\n6539#1:20543,2\n6734#1:20569,9\n6734#1:20590\n6739#1:20606,9\n6739#1:20627\n6739#1:20629,2\n6734#1:20633,2\n6334#1:20637,2\n6318#1:20654,9\n6318#1:20675\n6318#1:20680,2\n6782#1:20698,9\n6782#1:20719\n6787#1:20730,9\n6787#1:20751\n6787#1:20754,2\n6782#1:20763,2\n417#1:7158,6\n423#1:7194,6\n2271#1:7719,6\n2276#1:7755,6\n2284#1:7792,6\n2290#1:7828,6\n2329#1:7874,6\n2335#1:7911,6\n2648#1:8009,6\n2656#1:8047,6\n2665#1:8090,6\n2691#1:8146,6\n2699#1:8183,6\n2714#1:8232,6\n2728#1:8279,6\n2746#1:8323,6\n2788#1:8408,6\n2799#1:8440,6\n2804#1:8477,6\n2807#1:8515,6\n2821#1:8560,6\n2842#1:8618,6\n2848#1:8663,6\n2863#1:8695,6\n2868#1:8732,6\n2882#1:8773,6\n3716#1:8951,6\n3728#1:8988,6\n3791#1:9045,6\n3799#1:9083,6\n4667#1:9342,6\n4672#1:9378,6\n4680#1:9416,6\n4691#1:9452,6\n4717#1:9491,6\n4736#1:9533,6\n5257#1:9630,6\n5263#1:9675,6\n5281#1:9712,6\n441#1:10045,6\n449#1:10082,6\n467#1:10123,6\n480#1:10173,6\n497#1:10219,6\n502#1:10262,6\n521#1:10309,6\n539#1:10357,6\n592#1:10411,6\n600#1:10457,6\n613#1:10494,6\n624#1:10533,6\n648#1:10591,6\n661#1:10628,6\n672#1:10667,6\n747#1:10753,6\n759#1:10790,6\n777#1:10854,6\n788#1:10900,6\n802#1:10936,6\n812#1:10975,6\n813#1:11012,6\n834#1:11060,6\n852#1:11119,6\n866#1:11155,6\n876#1:11194,6\n894#1:11237,6\n975#1:11309,6\n1340#1:11384,6\n1346#1:11416,6\n1351#1:11454,6\n1356#1:11493,6\n1369#1:11536,6\n1399#1:11583,6\n1418#1:11628,6\n1470#1:11675,6\n1478#1:11712,6\n1479#1:11747,6\n1512#1:11801,6\n1517#1:11845,6\n1534#1:11898,6\n1611#1:11951,6\n1617#1:11983,6\n1623#1:12020,6\n1624#1:12059,6\n1631#1:12096,6\n1645#1:12145,6\n1661#1:12189,6\n1679#1:12234,6\n1699#1:12281,6\n1700#1:12318,6\n1716#1:12362,6\n1736#1:12412,6\n1745#1:12449,6\n1772#1:12499,6\n1780#1:12535,6\n1781#1:12567,6\n1786#1:12604,6\n1801#1:12645,6\n1821#1:12692,6\n1843#1:12730,6\n1867#1:12788,6\n1876#1:12820,6\n1881#1:12857,6\n1903#1:12910,6\n1923#1:12948,6\n1929#1:12992,6\n1943#1:13029,6\n1964#1:13082,6\n1982#1:13130,6\n2001#1:13178,6\n2377#1:13314,6\n2406#1:13354,6\n2459#1:13396,6\n2489#1:13443,6\n2507#1:13480,6\n2534#1:13523,6\n2540#1:13562,6\n2556#1:13611,6\n2586#1:13655,6\n2592#1:13694,6\n2599#1:13731,6\n2607#1:13770,6\n2624#1:13819,6\n2924#1:13882,6\n2930#1:13914,6\n2935#1:13951,6\n2951#1:13996,6\n2979#1:14038,6\n2987#1:14080,6\n3001#1:14126,6\n3015#1:14172,6\n3029#1:14218,6\n3043#1:14264,6\n3061#1:14311,6\n3088#1:14363,6\n3101#1:14400,6\n3293#1:14451,6\n3301#1:14487,6\n3344#1:14532,6\n3352#1:14568,6\n3353#1:14600,6\n3368#1:14638,6\n3382#1:14680,6\n3411#1:14725,6\n3451#1:14779,6\n3459#1:14811,6\n3464#1:14847,6\n3500#1:14908,6\n3508#1:14944,6\n3509#1:14976,6\n3514#1:15012,6\n3571#1:15075,6\n3613#1:15134,6\n3653#1:15195,6\n3660#1:15232,6\n3882#1:15323,6\n3902#1:15364,6\n3947#1:15417,6\n3955#1:15454,6\n3988#1:15509,6\n3996#1:15547,6\n3997#1:15579,6\n4003#1:15617,6\n4022#1:15676,6\n4058#1:15734,6\n4073#1:15771,6\n4074#1:15808,6\n4269#1:15878,6\n4271#1:15916,6\n4316#1:15980,6\n4410#1:16058,6\n4416#1:16090,6\n4769#1:16212,6\n4839#1:16266,6\n4845#1:16302,6\n4849#1:16341,6\n4860#1:16383,6\n4878#1:16438,6\n4891#1:16470,6\n4896#1:16507,6\n4897#1:16544,6\n4907#1:16586,6\n4957#1:16650,6\n4971#1:16687,6\n4975#1:16723,6\n4999#1:16770,6\n5022#1:16811,6\n5057#1:16870,6\n5063#1:16906,6\n5067#1:16945,6\n5078#1:16987,6\n5119#1:17041,6\n5134#1:17087,6\n5199#1:17149,6\n5206#1:17194,6\n5229#1:17243,6\n5390#1:17319,6\n5398#1:17351,6\n5403#1:17387,6\n5409#1:17425,6\n5426#1:17471,6\n5444#1:17516,6\n5449#1:17552,6\n5450#1:17589,6\n5465#1:17633,6\n5501#1:17683,6\n5509#1:17723,6\n5546#1:17777,6\n5561#1:17818,6\n5569#1:17864,6\n5580#1:17896,6\n5602#1:17945,6\n5610#1:17985,6\n5620#1:18017,6\n5665#1:18078,6\n5673#1:18118,6\n5683#1:18150,6\n5728#1:18214,6\n5736#1:18250,6\n5750#1:18295,6\n5872#1:18375,6\n5877#1:18407,6\n5889#1:18451,6\n5959#1:18511,6\n5966#1:18548,6\n6016#1:18595,6\n6024#1:18633,6\n6025#1:18670,6\n6035#1:18712,6\n6045#1:18754,6\n6061#1:18805,6\n6074#1:18842,6\n5943#1:18892,6\n6137#1:18937,6\n6144#1:18974,6\n6194#1:19021,6\n6202#1:19059,6\n6203#1:19096,6\n6213#1:19138,6\n6223#1:19180,6\n6239#1:19230,6\n6121#1:19278,6\n6600#1:19332,6\n6608#1:19364,6\n6613#1:19400,6\n6618#1:19436,6\n6640#1:19477,6\n6657#1:19519,6\n6658#1:19556,6\n6669#1:19605,6\n6684#1:19660,6\n6713#1:19714,6\n6334#1:19762,6\n6338#1:19799,6\n6348#1:19831,6\n6353#1:19867,6\n6357#1:19904,6\n6368#1:19946,6\n6383#1:19990,6\n6403#1:20043,6\n6412#1:20079,6\n6413#1:20111,6\n6418#1:20147,6\n6439#1:20191,6\n6457#1:20232,6\n6478#1:20275,6\n6502#1:20325,6\n6518#1:20364,6\n6524#1:20401,6\n6539#1:20448,6\n6546#1:20486,6\n6555#1:20523,6\n6734#1:20582,6\n6739#1:20619,6\n6318#1:20667,6\n6782#1:20711,6\n6787#1:20743,6\n423#1:7168\n423#1:7169,6\n423#1:7203\n423#1:7235\n2276#1:7729\n2276#1:7730,6\n2276#1:7764\n2728#1:8253\n2728#1:8254,6\n2728#1:8288\n2728#1:8360\n2807#1:8488\n2807#1:8489,7\n2807#1:8524\n2807#1:8528\n2868#1:8705\n2868#1:8706,7\n2868#1:8741\n2868#1:8745\n2276#1:8813\n3728#1:8961\n3728#1:8962,7\n3728#1:8997\n3728#1:9002\n4672#1:9352\n4672#1:9353,6\n4672#1:9387\n4736#1:9507\n4736#1:9508,6\n4736#1:9542\n4736#1:9546\n4672#1:9740\n467#1:10097\n467#1:10098,6\n467#1:10132\n467#1:10190\n624#1:10507\n624#1:10508,6\n624#1:10542\n624#1:10546\n672#1:10641\n672#1:10642,6\n672#1:10676\n672#1:10680\n812#1:10949\n812#1:10950,6\n812#1:10984\n812#1:11031\n876#1:11168\n876#1:11169,6\n876#1:11203\n876#1:11208\n975#1:11283\n975#1:11284,6\n975#1:11318\n975#1:11323\n1340#1:11358\n1340#1:11359,6\n1340#1:11393\n1418#1:11602\n1418#1:11603,6\n1418#1:11637\n1418#1:11644\n1478#1:11685\n1478#1:11686,7\n1478#1:11721\n1478#1:11773\n1340#1:11923\n1611#1:11925\n1611#1:11926,6\n1611#1:11960\n1699#1:12255\n1699#1:12256,6\n1699#1:12290\n1699#1:12381\n1745#1:12422\n1745#1:12423,7\n1745#1:12458\n1745#1:12463\n1780#1:12509\n1780#1:12510,6\n1780#1:12544\n1786#1:12577\n1786#1:12578,7\n1786#1:12613\n1786#1:12617\n1801#1:12618\n1801#1:12619,7\n1801#1:12654\n1801#1:12658\n1780#1:12747\n1611#1:13200\n2459#1:13370\n2459#1:13371,6\n2459#1:13405\n2459#1:13853\n2924#1:13856\n2924#1:13857,6\n2924#1:13891\n2924#1:14423\n3293#1:14425\n3293#1:14426,6\n3293#1:14460\n3352#1:14542\n3352#1:14543,6\n3352#1:14577\n3352#1:14743\n3464#1:14821\n3464#1:14822,6\n3464#1:14856\n3464#1:14860\n3508#1:14918\n3508#1:14919,6\n3508#1:14953\n3514#1:14986\n3514#1:14987,6\n3514#1:15021\n3514#1:15025\n3508#1:15042\n3660#1:15205\n3660#1:15206,7\n3660#1:15241\n3660#1:15246\n3293#1:15257\n3882#1:15297\n3882#1:15298,6\n3882#1:15332\n3882#1:15337\n3902#1:15339\n3902#1:15340,5\n3902#1:15373\n3996#1:15520\n3996#1:15521,7\n3996#1:15556\n3996#1:15638\n4073#1:15744\n4073#1:15745,7\n4073#1:15780\n4073#1:15834\n3902#1:15842\n4269#1:15851\n4269#1:15852,7\n4269#1:15887\n4269#1:15943\n4316#1:15953\n4316#1:15954,7\n4316#1:15989\n4316#1:16000\n4410#1:16032\n4410#1:16033,6\n4410#1:16067\n4410#1:16136\n4839#1:16240\n4839#1:16241,6\n4839#1:16275\n4860#1:16356\n4860#1:16357,7\n4860#1:16392\n4860#1:16396\n4907#1:16559\n4907#1:16560,7\n4907#1:16595\n4907#1:16599\n4971#1:16661\n4971#1:16662,6\n4971#1:16696\n4971#1:16742\n4999#1:16743\n4999#1:16744,7\n4999#1:16779\n4999#1:16828\n4839#1:16836\n5057#1:16844\n5057#1:16845,6\n5057#1:16879\n5078#1:16960\n5078#1:16961,7\n5078#1:16996\n5078#1:17000\n5057#1:17264\n5390#1:17294\n5390#1:17295,5\n5390#1:17328\n5409#1:17398\n5409#1:17399,7\n5409#1:17434\n5409#1:17439\n5449#1:17526\n5449#1:17527,6\n5449#1:17561\n5449#1:17656\n5501#1:17657\n5501#1:17658,6\n5501#1:17692\n5501#1:17746\n5561#1:17792\n5561#1:17793,6\n5561#1:17827\n5561#1:17918\n5602#1:17919\n5602#1:17920,6\n5602#1:17954\n5602#1:18051\n5665#1:18052\n5665#1:18053,6\n5665#1:18087\n5665#1:18184\n5750#1:18268\n5750#1:18269,7\n5750#1:18304\n5750#1:18309\n5390#1:18329\n5872#1:18349\n5872#1:18350,6\n5872#1:18384\n5872#1:18481\n5959#1:18485\n5959#1:18486,6\n5959#1:18520\n6024#1:18606\n6024#1:18607,7\n6024#1:18642\n6024#1:18772\n5959#1:18865\n6137#1:18911\n6137#1:18912,6\n6137#1:18946\n6202#1:19032\n6202#1:19033,7\n6202#1:19068\n6202#1:19198\n6239#1:19205\n6239#1:19206,5\n6239#1:19239\n6239#1:19247\n6137#1:19251\n6657#1:19492\n6657#1:19493,7\n6657#1:19528\n6657#1:19682\n6713#1:19687\n6713#1:19688,7\n6713#1:19723\n6713#1:19727\n6334#1:19736\n6334#1:19737,6\n6334#1:19771\n6368#1:19919\n6368#1:19920,7\n6368#1:19955\n6368#1:19959\n6412#1:20053\n6412#1:20054,6\n6412#1:20088\n6418#1:20121\n6418#1:20122,6\n6418#1:20156\n6418#1:20161\n6412#1:20293\n6524#1:20374\n6524#1:20375,7\n6524#1:20410\n6524#1:20417\n6555#1:20496\n6555#1:20497,7\n6555#1:20532\n6555#1:20538\n6334#1:20640\n6782#1:20685\n6782#1:20686,6\n6782#1:20720\n6782#1:20766\n433#1:7204\n560#1:7205\n587#1:7224\n588#1:7225\n1323#1:7390\n1326#1:7391\n1333#1:7392\n1336#1:7399\n1338#1:7400\n1604#1:7419\n1605#1:7420\n1605#1:7421\n1607#1:7428\n1609#1:7429\n2288#1:7765\n2323#1:7844\n2326#1:7845\n2331#1:7846\n2333#1:7847\n2338#1:7921\n2342#1:7922\n2445#1:7963\n2451#1:7964\n2453#1:7971\n2454#1:7972\n2454#1:7973\n2455#1:7974\n2457#1:7975\n2645#1:7976\n2651#1:7977\n2667#1:8057\n2670#1:8058\n2677#1:8100\n2693#1:8111\n2695#1:8112\n2697#1:8119\n2701#1:8193\n2716#1:8206\n2731#1:8252\n2743#1:8289\n2748#1:8290\n2750#1:8291\n2750#1:8292\n2752#1:8299\n2754#1:8300\n2758#1:8333\n2770#1:8338\n2785#1:8365\n2791#1:8366\n2793#1:8373\n2795#1:8374\n2797#1:8381\n2806#1:8487\n2828#1:8570\n2833#1:8571\n2839#1:8584\n2845#1:8585\n2851#1:8628\n2852#1:8629\n2858#1:8630\n2860#1:8637\n2889#1:8783\n2894#1:8784\n2906#1:8801\n2911#1:8802\n2917#1:8803\n2918#1:8804\n2919#1:8805\n2919#1:8806\n2920#1:8807\n2922#1:8808\n3120#1:8809\n3718#1:8917\n3719#1:8918\n3725#1:8925\n3733#1:8998\n3748#1:9007\n3749#1:9008\n3750#1:9009\n3793#1:9010\n3795#1:9011\n3797#1:9018\n3801#1:9055\n3804#1:9093\n3810#1:9094\n3846#1:9103\n4204#1:9169\n4250#1:9170\n4408#1:9189\n4683#1:9388\n4689#1:9389\n4714#1:9462\n4719#1:9463\n4720#1:9464\n4722#1:9465\n4729#1:9501\n4733#1:9506\n4788#1:9561\n4794#1:9562\n4795#1:9563\n4825#1:9570\n4831#1:9571\n4833#1:9578\n4834#1:9579\n4834#1:9580\n4835#1:9581\n4837#1:9582\n5043#1:9583\n5049#1:9584\n5051#1:9591\n5052#1:9592\n5052#1:9593\n5053#1:9594\n5055#1:9595\n5254#1:9596\n5260#1:9597\n5266#1:9640\n5267#1:9641\n5273#1:9642\n5273#1:9643\n5289#1:9722\n5291#1:9723\n5302#1:9736\n444#1:10022\n451#1:10055\n454#1:10056\n462#1:10092\n479#1:10133\n482#1:10134\n484#1:10135\n485#1:10136,11\n498#1:10191\n504#1:10229\n507#1:10230\n523#1:10276\n526#1:10277\n534#1:10319\n541#1:10324\n544#1:10325\n596#1:10385\n603#1:10421\n609#1:10422\n609#1:10423\n611#1:10430\n618#1:10504\n621#1:10505\n623#1:10506\n651#1:10555\n657#1:10556\n657#1:10557\n659#1:10564\n666#1:10638\n669#1:10639\n671#1:10640\n749#1:10717\n755#1:10718\n755#1:10719\n757#1:10726\n761#1:10800\n702#1:10815\n782#1:10828\n792#1:10864\n798#1:10865\n798#1:10866\n800#1:10873\n807#1:10946\n809#1:10947\n811#1:10948\n815#1:11022\n824#1:11027\n833#1:11032\n836#1:11033\n838#1:11034\n845#1:11070\n856#1:11083\n862#1:11084\n862#1:11085\n864#1:11092\n871#1:11165\n873#1:11166\n875#1:11167\n884#1:11204\n893#1:11209\n896#1:11210\n898#1:11211\n905#1:11247\n978#1:11282\n992#1:11319\n1343#1:11357\n1352#1:11426\n1358#1:11464\n1363#1:11465\n1364#1:11466\n1371#1:11507\n1373#1:11508\n1374#1:11509\n1401#1:11554\n1403#1:11555\n1404#1:11556\n1415#1:11601\n1433#1:11638\n1442#1:11639\n1453#1:11640\n1463#1:11645\n1473#1:11646\n1475#1:11647\n1476#1:11648\n1496#1:11761\n1501#1:11768\n1502#1:11769\n1509#1:11778\n1519#1:11811\n1521#1:11818\n1525#1:11855\n1528#1:11856\n1530#1:11857\n1536#1:11862\n1538#1:11863\n1538#1:11864\n1540#1:11871\n1546#1:11916\n1553#1:11917\n1557#1:11918\n1558#1:11919\n1614#1:11924\n1626#1:12030\n1628#1:12031\n1629#1:12032\n1633#1:12106\n1644#1:12115\n1647#1:12116\n1649#1:12117\n1650#1:12118\n1660#1:12159\n1663#1:12160\n1665#1:12161\n1666#1:12162\n1684#1:12244\n1686#1:12245\n1696#1:12254\n1705#1:12328\n1707#1:12329\n1715#1:12334\n1721#1:12372\n1723#1:12373\n1733#1:12382\n1739#1:12383\n1741#1:12384\n1741#1:12385\n1742#1:12386\n1756#1:12459\n1769#1:12468\n1775#1:12469\n1777#1:12470\n1777#1:12471\n1778#1:12472\n1818#1:12663\n1824#1:12664\n1825#1:12665\n1832#1:12702\n1841#1:12707\n1864#1:12752\n1870#1:12753\n1872#1:12754\n1874#1:12761\n1883#1:12867\n1902#1:12880\n1906#1:12881\n1908#1:12882\n1909#1:12883\n1920#1:12924\n1925#1:12925\n1932#1:12958\n1933#1:12959\n1939#1:12960\n1951#1:13039\n1953#1:13040\n1966#1:13049\n1969#1:13050\n1977#1:13092\n1984#1:13097\n1987#1:13098\n1995#1:13140\n2003#1:13145\n2006#1:13146\n2014#1:13188\n2379#1:13288\n2408#1:13328\n2417#1:13364\n2462#1:13369\n2488#1:13406\n2491#1:13407\n2500#1:13408\n2502#1:13409\n2505#1:13416\n2521#1:13490\n2526#1:13491\n2531#1:13500\n2542#1:13533\n2544#1:13534\n2545#1:13535\n2558#1:13576\n2560#1:13577\n2562#1:13584\n2566#1:13621\n2571#1:13622\n2573#1:13623\n2583#1:13632\n2594#1:13665\n2596#1:13666\n2597#1:13667\n2606#1:13741\n2609#1:13742\n2611#1:13743\n2632#1:13835\n2682#1:13854\n2927#1:13855\n2940#1:13961\n2942#1:13962\n2953#1:13967\n2955#1:13968\n2956#1:13969\n2967#1:14014\n2976#1:14015\n2989#1:14048\n3003#1:14094\n3017#1:14140\n3031#1:14186\n3045#1:14232\n3063#1:14278\n3066#1:14279\n3085#1:14329\n3091#1:14330\n3092#1:14331\n3106#1:14410\n3108#1:14411\n3296#1:14424\n3303#1:14461\n3315#1:14501\n3339#1:14502\n3347#1:14503\n3349#1:14504\n3350#1:14505\n3370#1:14610\n3372#1:14611\n3384#1:14652\n3386#1:14653\n3399#1:14698\n3410#1:14699\n3434#1:14739\n3447#1:14748\n3454#1:14749\n3456#1:14750\n3456#1:14751\n3457#1:14752\n3488#1:14861\n3489#1:14862\n3496#1:14877\n3503#1:14878\n3505#1:14879\n3505#1:14880\n3506#1:14881\n3539#1:15026\n3540#1:15027\n3546#1:15038\n3557#1:15047\n3568#1:15048\n3573#1:15049\n3610#1:15107\n3615#1:15108\n3652#1:15166\n3656#1:15167\n3658#1:15168\n3667#1:15242\n3682#1:15251\n3689#1:15252\n3690#1:15253\n3287#1:15258\n3288#1:15259\n3290#1:15260\n3291#1:15261\n4184#1:15287\n4177#1:15288\n4181#1:15289\n4198#1:15296\n3884#1:15333\n3906#1:15338\n3917#1:15374\n3936#1:15381\n3950#1:15388\n3952#1:15389\n3953#1:15390\n3957#1:15464\n3977#1:15473\n3991#1:15480\n3993#1:15481\n3994#1:15482\n3996#1:15519\n4005#1:15589\n4007#1:15590\n4026#1:15649\n4036#1:15692\n4061#1:15697\n4064#1:15698\n4064#1:15699\n4065#1:15700\n4071#1:15707\n4085#1:15824\n4096#1:15829\n4102#1:15830\n4239#1:15843\n4269#1:15850\n4271#1:15888\n4278#1:15927\n4279#1:15928\n4318#1:15990\n4413#1:16031\n4436#1:16104\n4445#1:16105\n4452#1:16112\n4456#1:16113\n4494#1:16120\n4509#1:16124\n4510#1:16125\n4568#1:16132\n4771#1:16185\n4774#1:16186\n4781#1:16222\n4842#1:16239\n4851#1:16312\n4852#1:16313\n4854#1:16314\n4854#1:16315\n4859#1:16355\n4875#1:16401\n4881#1:16402\n4887#1:16403\n4887#1:16404\n4889#1:16411\n4899#1:16517\n4900#1:16518\n4906#1:16558\n4931#1:16612\n4940#1:16613\n4947#1:16614\n4954#1:16621\n4960#1:16622\n4966#1:16623\n4967#1:16624\n4973#1:16660\n4977#1:16697\n4984#1:16737\n4991#1:16738\n5010#1:16780\n5021#1:16781\n5024#1:16782\n5026#1:16783\n5027#1:16784\n5060#1:16843\n5069#1:16916\n5070#1:16917\n5072#1:16918\n5072#1:16919\n5077#1:16959\n5093#1:17005\n5102#1:17006\n5109#1:17007\n5116#1:17014\n5121#1:17015\n5137#1:17052\n5140#1:17053\n5142#1:17054\n5152#1:17061\n5165#1:17106\n5174#1:17107\n5182#1:17108\n5189#1:17115\n5198#1:17116\n5203#1:17123\n5209#1:17159\n5212#1:17160\n5214#1:17161\n5217#1:17168\n5232#1:17208\n5235#1:17209\n5237#1:17210\n5240#1:17217\n5393#1:17292\n5395#1:17293\n5408#1:17397\n5416#1:17435\n5428#1:17444\n5430#1:17445\n5438#1:17481\n5446#1:17490\n5464#1:17603\n5468#1:17604\n5469#1:17605\n5471#1:17606\n5472#1:17607\n5508#1:17693\n5512#1:17694\n5513#1:17695\n5515#1:17696\n5516#1:17697\n5551#1:17787\n5568#1:17828\n5572#1:17829\n5573#1:17830\n5575#1:17831\n5577#1:17838\n5595#1:17906\n5609#1:17955\n5613#1:17956\n5614#1:17957\n5616#1:17958\n5617#1:17959\n5650#1:18039\n5672#1:18088\n5676#1:18089\n5677#1:18090\n5679#1:18091\n5680#1:18092\n5713#1:18172\n5731#1:18185\n5733#1:18186\n5734#1:18187\n5747#1:18260\n5749#1:18267\n5757#1:18305\n5796#1:18324\n5797#1:18325\n5385#1:18330\n5386#1:18331\n5388#1:18332\n5875#1:18348\n5883#1:18417\n5887#1:18422\n5892#1:18423\n5893#1:18424\n5895#1:18425\n5913#1:18471\n5868#1:18482\n5869#1:18483\n5962#1:18484\n5968#1:18521\n5975#1:18522\n5981#1:18562\n5991#1:18563\n6003#1:18564\n6013#1:18565\n6019#1:18566\n6021#1:18567\n6022#1:18568\n6024#1:18605\n6027#1:18680\n6037#1:18722\n6047#1:18764\n6058#1:18777\n6064#1:18778\n6065#1:18779\n6079#1:18852\n6089#1:18861\n5947#1:18866\n5953#1:18902\n5954#1:18903\n5956#1:18904\n5957#1:18905\n6140#1:18910\n6146#1:18947\n6153#1:18948\n6159#1:18988\n6169#1:18989\n6181#1:18990\n6191#1:18991\n6197#1:18992\n6199#1:18993\n6200#1:18994\n6202#1:19031\n6205#1:19106\n6215#1:19148\n6225#1:19190\n6236#1:19203\n6241#1:19204\n6247#1:19240\n6252#1:19241\n6253#1:19242\n6270#1:19243\n6125#1:19252\n6131#1:19288\n6132#1:19289\n6134#1:19290\n6135#1:19291\n6603#1:19302\n6605#1:19303\n6605#1:19304\n6606#1:19305\n6619#1:19410\n6630#1:19450\n6642#1:19451\n6655#1:19491\n6668#1:19566\n6671#1:19567\n6673#1:19568,11\n6683#1:19619\n6686#1:19620\n6688#1:19621\n6688#1:19622\n6689#1:19623,11\n6702#1:19678\n6346#1:19772\n6359#1:19877\n6362#1:19878\n6367#1:19918\n6385#1:19964\n6395#1:20000\n6406#1:20013\n6407#1:20014\n6409#1:20015\n6410#1:20016\n6427#1:20157\n6441#1:20162\n6443#1:20163\n6444#1:20164\n6455#1:20209\n6476#1:20246\n6481#1:20247\n6482#1:20248\n6489#1:20285\n6505#1:20298\n6527#1:20411\n6528#1:20412\n6530#1:20413\n6543#1:20422\n6549#1:20458\n6551#1:20459\n6552#1:20460\n6557#1:20533\n6564#1:20534\n6579#1:20547\n6580#1:20548\n6737#1:20555\n6742#1:20592\n6743#1:20593\n6322#1:20641\n6329#1:20677\n6331#1:20678\n6332#1:20679\n6785#1:20684\n6793#1:20753\n6797#1:20758\n6799#1:20759\n6806#1:20760\n6811#1:20761\n6813#1:20762\n6776#1:20767\n6777#1:20768\n6779#1:20769\n6780#1:20770\n2290#1:7802\n2290#1:7803,6\n2290#1:7837\n2335#1:7884\n2335#1:7885,7\n2335#1:7920\n2335#1:7926\n2290#1:7958\n2656#1:8025,3\n2656#1:8056\n2699#1:8156\n2699#1:8157,7\n2699#1:8192\n2699#1:8197\n2656#1:8205\n2746#1:8301,3\n2746#1:8332\n2746#1:8337\n2799#1:8418,3\n2799#1:8449\n2804#1:8450\n2804#1:8451,7\n2804#1:8486\n2804#1:8532\n2821#1:8533\n2821#1:8534,7\n2821#1:8569\n2821#1:8575\n2799#1:8579\n2863#1:8673,3\n2863#1:8704\n2882#1:8746\n2882#1:8747,7\n2882#1:8782\n2882#1:8788\n2863#1:8792\n3799#1:9056\n3799#1:9057,7\n3799#1:9092\n3799#1:9098\n4691#1:9426\n4691#1:9427,6\n4691#1:9461\n4691#1:9556\n5281#1:9685\n5281#1:9686,7\n5281#1:9721\n5281#1:9727\n441#1:10023,3\n441#1:10054\n497#1:10192\n497#1:10193,7\n497#1:10228\n497#1:10374\n441#1:10378\n592#1:10386\n592#1:10387,5\n592#1:10420\n613#1:10467\n613#1:10468,7\n613#1:10503\n613#1:10550\n661#1:10601\n661#1:10602,7\n661#1:10637\n661#1:10684\n592#1:10692\n759#1:10763\n759#1:10764,7\n759#1:10799\n759#1:10804\n777#1:10829\n777#1:10830,5\n777#1:10863\n802#1:10910\n802#1:10911,6\n802#1:10945\n813#1:10985\n813#1:10986,7\n813#1:11021\n813#1:11026\n802#1:11078\n866#1:11129\n866#1:11130,6\n866#1:11164\n866#1:11255\n777#1:11263\n1346#1:11394,3\n1346#1:11425\n1351#1:11427\n1351#1:11428,7\n1351#1:11463\n1351#1:11553\n1346#1:11600\n1479#1:11722\n1479#1:11723,5\n1479#1:11756\n1479#1:11760\n1512#1:11779,3\n1512#1:11810\n1517#1:11819\n1517#1:11820,6\n1517#1:11854\n1517#1:11861\n1512#1:11915\n1617#1:11961,3\n1617#1:11992\n1623#1:11993\n1623#1:11994,7\n1623#1:12029\n1631#1:12069\n1631#1:12070,7\n1631#1:12105\n1631#1:12110\n1623#1:12206\n1679#1:12207\n1679#1:12208,7\n1679#1:12243\n1679#1:12249\n1617#1:12253\n1700#1:12291\n1700#1:12292,7\n1700#1:12327\n1700#1:12333\n1716#1:12335\n1716#1:12336,7\n1716#1:12371\n1716#1:12377\n1781#1:12545,3\n1781#1:12576\n1781#1:12662\n1843#1:12708,3\n1843#1:12739\n1843#1:12743\n1876#1:12798,3\n1876#1:12829\n1881#1:12830\n1881#1:12831,7\n1881#1:12866\n1881#1:12871\n1876#1:12875\n1923#1:12926,3\n1923#1:12957\n1943#1:13002\n1943#1:13003,7\n1943#1:13038\n1943#1:13044\n1923#1:13196\n2507#1:13453\n2507#1:13454,7\n2507#1:13489\n2507#1:13495\n2534#1:13501,3\n2534#1:13532\n2556#1:13585\n2556#1:13586,6\n2556#1:13620\n2556#1:13627\n2534#1:13631\n2586#1:13633,3\n2586#1:13664\n2599#1:13704\n2599#1:13705,7\n2599#1:13740\n2599#1:13787\n2624#1:13792\n2624#1:13793,7\n2624#1:13828\n2624#1:13845\n2586#1:13849\n2930#1:13892,3\n2930#1:13923\n2935#1:13924\n2935#1:13925,7\n2935#1:13960\n2935#1:13966\n2930#1:14013\n2979#1:14016,3\n2979#1:14047\n2979#1:14328\n3101#1:14373\n3101#1:14374,7\n3101#1:14409\n3101#1:14415\n3353#1:14578,3\n3353#1:14609\n3353#1:14697\n3411#1:14700\n3411#1:14701,5\n3411#1:14734\n3411#1:14738\n3459#1:14789,3\n3459#1:14820\n3459#1:14872\n3509#1:14954,3\n3509#1:14985\n3509#1:15037\n3571#1:15050\n3571#1:15051,5\n3571#1:15084\n3571#1:15106\n3613#1:15109\n3613#1:15110,5\n3613#1:15143\n3613#1:15165\n3955#1:15427\n3955#1:15428,7\n3955#1:15463\n3955#1:15468\n3997#1:15557,3\n3997#1:15588\n3997#1:15634\n4022#1:15650\n4022#1:15651,6\n4022#1:15685\n4022#1:15696\n4074#1:15781\n4074#1:15782,7\n4074#1:15817\n4074#1:15828\n4271#1:15889\n4271#1:15890,7\n4271#1:15925\n4271#1:15939\n4416#1:16068,3\n4416#1:16099\n4416#1:16103\n4845#1:16276\n4845#1:16277,6\n4845#1:16311\n4845#1:16400\n4891#1:16448,3\n4891#1:16479\n4896#1:16480\n4896#1:16481,7\n4896#1:16516\n4896#1:16603\n4891#1:16607\n5063#1:16880\n5063#1:16881,6\n5063#1:16915\n5063#1:17004\n5119#1:17016\n5119#1:17017,5\n5119#1:17050\n5119#1:17105\n5199#1:17124\n5199#1:17125,5\n5199#1:17158\n5199#1:17260\n5398#1:17329,3\n5398#1:17360\n5403#1:17361\n5403#1:17362,6\n5403#1:17396\n5403#1:17443\n5398#1:17489\n5444#1:17491\n5444#1:17492,5\n5444#1:17525\n5450#1:17562\n5450#1:17563,7\n5450#1:17598\n5450#1:17602\n5444#1:17750\n5546#1:17751\n5546#1:17752,6\n5546#1:17786\n5546#1:17791\n5580#1:17874,3\n5580#1:17905\n5580#1:17910\n5620#1:17995,3\n5620#1:18026\n5620#1:18043\n5683#1:18128,3\n5683#1:18159\n5683#1:18176\n5736#1:18224\n5736#1:18225,6\n5736#1:18259\n5736#1:18313\n5877#1:18385,3\n5877#1:18416\n5877#1:18421\n6025#1:18643\n6025#1:18644,7\n6025#1:18679\n6025#1:18684\n6035#1:18685\n6035#1:18686,7\n6035#1:18721\n6035#1:18726\n6045#1:18727\n6045#1:18728,7\n6045#1:18763\n6045#1:18768\n6074#1:18815\n6074#1:18816,7\n6074#1:18851\n6074#1:18856\n6203#1:19069\n6203#1:19070,7\n6203#1:19105\n6203#1:19110\n6213#1:19111\n6213#1:19112,7\n6213#1:19147\n6213#1:19152\n6223#1:19153\n6223#1:19154,7\n6223#1:19189\n6223#1:19194\n6608#1:19342,3\n6608#1:19373\n6613#1:19374\n6613#1:19375,6\n6613#1:19409\n6658#1:19529\n6658#1:19530,7\n6658#1:19565\n6658#1:19677\n6613#1:19686\n6608#1:19731\n6348#1:19809,3\n6348#1:19840\n6353#1:19841\n6353#1:19842,6\n6353#1:19876\n6353#1:19963\n6348#1:20008\n6413#1:20089,3\n6413#1:20120\n6413#1:20208\n6457#1:20210,3\n6457#1:20241\n6457#1:20245\n6787#1:20721,3\n6787#1:20752\n6787#1:20757\n240#1:9929\n240#1:9930,2\n241#1:9932\n241#1:9933,2\n242#1:9935\n242#1:9936,2\n244#1:9938\n244#1:9939,2\n245#1:9941\n245#1:9942,2\n246#1:9944\n246#1:9945,2\n249#1:9953\n249#1:9954,2\n250#1:9956\n250#1:9957,2\n251#1:9959\n251#1:9960,2\n254#1:9962\n254#1:9963,2\n255#1:9965\n255#1:9966,2\n256#1:9968\n256#1:9969,2\n257#1:9971\n257#1:9972,2\n258#1:9974\n258#1:9975,2\n259#1:9977\n259#1:9978,2\n260#1:9980\n260#1:9981,2\n261#1:9983\n261#1:9984,2\n263#1:9986\n263#1:9987,2\n264#1:9989\n264#1:9990,2\n265#1:9992\n265#1:9993,2\n266#1:9995\n266#1:9996,2\n323#1:9998\n323#1:9999,2\n371#1:10001\n371#1:10002,2\n373#1:10007\n373#1:10008,2\n374#1:10010\n374#1:10011,2\n375#1:10013\n375#1:10014,2\n376#1:10016\n376#1:10017,2\n377#1:10019\n377#1:10020,2\n2043#1:13201\n2043#1:13202,2\n2047#1:13207\n2047#1:13208,2\n2048#1:13210\n2048#1:13211,2\n2049#1:13213\n2049#1:13214,2\n2050#1:13216\n2050#1:13217,2\n2051#1:13219\n2051#1:13220,2\n2052#1:13222\n2052#1:13223,2\n2109#1:13225\n2109#1:13226,2\n2110#1:13228\n2110#1:13229,2\n2111#1:13231\n2111#1:13232,2\n2112#1:13234\n2112#1:13235,2\n2113#1:13237\n2113#1:13238,2\n2121#1:13249\n2121#1:13250,2\n2124#1:13252\n2124#1:13253,2\n2126#1:13258\n2126#1:13259,2\n2128#1:13261\n2128#1:13262,2\n2129#1:13264\n2129#1:13265,2\n2132#1:13267\n2132#1:13268,2\n2133#1:13270\n2133#1:13271,2\n2134#1:13273\n2134#1:13274,2\n2150#1:13285\n2150#1:13286,2\n3835#1:15262\n3864#1:15263\n3864#1:15264,2\n3865#1:15266\n3865#1:15267,2\n3866#1:15269\n3866#1:15270,2\n3867#1:15272\n3867#1:15273,2\n3868#1:15275\n3868#1:15276,2\n3869#1:15278\n3869#1:15279,2\n3871#1:15281\n3871#1:15282,2\n3872#1:15284\n3872#1:15285,2\n4311#1:15944\n4311#1:15945,2\n4401#1:16001\n4401#1:16002,2\n4402#1:16004\n4402#1:16005,2\n4586#1:16137\n4586#1:16138,2\n4590#1:16143\n4590#1:16144,2\n4591#1:16146\n4591#1:16147,2\n4592#1:16149\n4592#1:16150,2\n4593#1:16152\n4593#1:16153,2\n4594#1:16155\n4594#1:16156,2\n4649#1:16158\n4649#1:16159,2\n4650#1:16161\n4650#1:16162,2\n4651#1:16164\n4651#1:16165,2\n4652#1:16167\n4652#1:16168,2\n4653#1:16170\n4653#1:16171,2\n4654#1:16173\n4654#1:16174,2\n4655#1:16176\n4655#1:16177,2\n4656#1:16179\n4656#1:16180,2\n4657#1:16182\n4657#1:16183,2\n5366#1:17265\n5366#1:17266,2\n5367#1:17268\n5367#1:17269,2\n5368#1:17271\n5368#1:17272,2\n5369#1:17274\n5369#1:17275,2\n5370#1:17277\n5370#1:17278,2\n5371#1:17280\n5371#1:17281,2\n5373#1:17283\n5373#1:17284,2\n5374#1:17286\n5374#1:17287,2\n5375#1:17289\n5375#1:17290,2\n5859#1:18333\n5859#1:18334,2\n6296#1:19296\n6296#1:19297,2\n6297#1:19299\n6297#1:19300,2\n247#1:9947\n247#1:9948,2\n248#1:9950\n248#1:9951,2\n372#1:10004\n372#1:10005,2\n2044#1:13204\n2044#1:13205,2\n2118#1:13240\n2118#1:13241,2\n2119#1:13243\n2119#1:13244,2\n2148#1:13279\n2148#1:13280,2\n4587#1:16140\n4587#1:16141,2\n1002#1:11345,12\n4466#1:16007,12\n4512#1:16019,12\n4798#1:16227,12\n5915#1:18336,12\n2120#1:13246\n2120#1:13247,2\n2125#1:13255\n2125#1:13256,2\n2146#1:13276\n2146#1:13277,2\n2149#1:13282\n2149#1:13283,2\n5105#1:16837\n5105#1:16838,5\n*E\n"})
/* loaded from: C:\Users\Lenovo\Documents\index\The Native\app\build\intermediates\project_dex_archive\debug\dexBuilderDebug\out\com\tasbih\digitaltasbih\ui\screens\groups\LiveDhikrScreenKt.dex */
public final class LiveDhikrScreenKt {
    private static final long DeepEmeraldGradTop = ColorKt.Color(4278460694L);
    private static final long DeepEmeraldGradMid = ColorKt.Color(4278532908L);
    private static final long DeepEmeraldGradEnd = ColorKt.Color(4278669885L);
    private static final long LuxuryGold = ColorKt.Color(4291405900L);
    private static final long BrightGold = ColorKt.Color(4292128567L);
    private static final long ShimmerGold = ColorKt.Color(4294759245L);
    private static final long ParchmentGold = ColorKt.Color(4294964664L);
    private static final long DarkCardBg = ColorKt.Color(4279179797L);
    private static final long DarkCardBorder = ColorKt.Color(1204398156);
    private static final long TextMutedGreen = ColorKt.Color(4288922808L);

    @NotNull
    private static final List<DhikrCatalogItem> FULL_DHIKR_CATALOG = CollectionsKt.listOf(new DhikrCatalogItem[]{new DhikrCatalogItem("subhan", "سُبْحَانَ اللّٰہ", "SubhanAllah", "Glory be to Allah", "tasbih", "Tasbih"), new DhikrCatalogItem("alhamd", "اَلْحَمْدُ لِلّٰہ", "Alhamdulillah", "All praise is for Allah", "tasbih", "Tahmeed"), new DhikrCatalogItem("akbar", "اَللّٰہُ اَکْبَر", "Allahu Akbar", "Allah is the Greatest", "tasbih", "Takbeer"), new DhikrCatalogItem("tahlil", "لَا اِلٰہَ اِلَّا اللّٰہ", "La ilaha illallah", "None has the right to be worshipped but Allah", "tasbih", "Tahleel"), new DhikrCatalogItem("istighfar", "اَسْتَغْفِرُ اللّٰہ", "Astaghfirullah", "I seek forgiveness of Allah", "istighfar", "Istighfar"), new DhikrCatalogItem("istighfar_long", "اَسْتَغْفِرُ اللّٰهَ رَبِّيْ مِنْ كُلِّ ذَنْبٍ وَّاَتُوْبُ اِلَيْهِ", "Astaghfirullah Rabbi min Kulli Zambin", "I ask forgiveness of Allah for every sin and turn to Him in repentance", "istighfar", "Istighfar"), new DhikrCatalogItem("sayyidul_istighfar", "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ خَلَقْتَنِي وَأَنَا عَبْدُكَ", "Sayyidul Istighfar", "O Allah, You are my Lord, none has the right to be worshipped but You", "istighfar", "Master Istighfar"), new DhikrCatalogItem("durood_short", "اَللّٰھُمَّ صَلِّ عَلٰی مُحَمَّدٍ وَّعَلٰٓی اٰلِ مُحَمَّدٍ", "Durood Shareef (Salawat)", "O Allah, send blessings upon Muhammad and upon the family of Muhammad", "durood", "Salawat"), new DhikrCatalogItem("durood_ibrahimi", "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ", "Durood-e-Ibrahimi", "O Allah, bestow Your favor upon Muhammad and the family of Muhammad", "durood", "Salawat"), new DhikrCatalogItem("durood_tunajjina", "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ صَلاَةً تُنَجِّينَا بِهَا مِنْ جَمِيعِ الأَهْوَالِ وَالآفَاتِ", "Durood-e-Tunajjina", "Prayer of Deliverance upon the Prophet", "durood", "Salawat"), new DhikrCatalogItem("hauqala", "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللّٰهِ", "La hawla wa la quwwata illa billah", "There is no power nor strength except with Allah", "tasbih", "Hauqala"), new DhikrCatalogItem("subhan_azeem", "سُبْحَانَ اللّٰهِ وَبِحَمْدِهِ سُبْحَانَ اللّٰهِ الْعَظِيْمِ", "SubhanAllahi wa bihamdihi SubhanAllahil Azeem", "Glory be to Allah and His praise, Glory be to Allah the Supreme", "tasbih", "Tasbih"), new DhikrCatalogItem("hasbunallah", "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ", "Hasbunallahu wa ni'mal wakeel", "Sufficient for us is Allah, and He is the best Disposer of affairs", "tasbih", "Trust"), new DhikrCatalogItem("ayatul_kursi", "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ", "Ayat-ul-Kursi", "Allah! There is no deity except Him, the Ever-Living, the Sustainer of all existence", "tasbih", "Quranic Ayah"), new DhikrCatalogItem("dua_yunus", "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ", "Dua-e-Yunus (Ayat-e-Kareema)", "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers", "istighfar", "Supplication"), new DhikrCatalogItem("rabbana_atina", "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً", "Rabbana Atina fid-Dunya", "Our Lord, give us in this world that which is good and in the Hereafter good", "tasbih", "Quranic Dua"), new DhikrCatalogItem("asma_1", "اَللهُ", "Allah", "The Greatest Name, The One and Only", "asma", "Name #1"), new DhikrCatalogItem("asma_2", "اَلرَّحْمٰن", "Ar-Rahman", "The Most Gracious", "asma", "Name #2"), new DhikrCatalogItem("asma_3", "اَلرَّحِيْم", "Ar-Raheem", "The Most Merciful", "asma", "Name #3"), new DhikrCatalogItem("asma_4", "اَلْمَلِك", "Al-Malik", "The Absolute Sovereign", "asma", "Name #4"), new DhikrCatalogItem("asma_5", "اَلْقُدُّوْس", "Al-Quddus", "The Most Holy, Free from all imperfections", "asma", "Name #5"), new DhikrCatalogItem("asma_6", "اَلسَّلَام", "As-Salam", "The Source of Peace and Safety", "asma", "Name #6"), new DhikrCatalogItem("asma_7", "اَلْمُؤْمِن", "Al-Mu'min", "The Guardian of Faith, Granter of Security", "asma", "Name #7"), new DhikrCatalogItem("asma_8", "اَلْمُهَيْمِن", "Al-Muhaymin", "The Protector, The Ever-Watching", "asma", "Name #8"), new DhikrCatalogItem("asma_9", "اَلْعَزِيْز", "Al-Aziz", "The All-Mighty, The Invincible", "asma", "Name #9"), new DhikrCatalogItem("asma_10", "اَلْجَبَّار", "Al-Jabbar", "The Compeller, The Restorer", "asma", "Name #10"), new DhikrCatalogItem("asma_11", "اَلْمُتَكَبِّر", "Al-Mutakabbir", "The Supreme, The Majestic", "asma", "Name #11"), new DhikrCatalogItem("asma_12", "اَلْخَالِق", "Al-Khaliq", "The Creator, The Maker", "asma", "Name #12"), new DhikrCatalogItem("asma_13", "اَلْبَارِئ", "Al-Bari'", "The Evolver, The Maker of Order", "asma", "Name #13"), new DhikrCatalogItem("asma_14", "اَلْمُصَوِّر", "Al-Musawwir", "The Fashioner, The Shaper of Beauty", "asma", "Name #14"), new DhikrCatalogItem("asma_15", "اَلْغَفَّار", "Al-Ghaffar", "The Constant Forgiver", "asma", "Name #15"), new DhikrCatalogItem("asma_16", "اَلْقَهَّار", "Al-Qahhar", "The Subduer, The Dominant", "asma", "Name #16"), new DhikrCatalogItem("asma_17", "اَلْوَهَّاب", "Al-Wahhab", "The Bestower, The Giver of Gifts", "asma", "Name #17"), new DhikrCatalogItem("asma_18", "اَلرَّزَّاق", "Ar-Razzaq", "The Provider, The Sustainer", "asma", "Name #18"), new DhikrCatalogItem("asma_19", "اَلْفَتَّاح", "Al-Fattah", "The Opener, The Reliever", "asma", "Name #19"), new DhikrCatalogItem("asma_20", "اَلْعَلِيْم", "Al-Aleem", "The All-Knowing, The Omniscient", "asma", "Name #20")});

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CampaignCard$lambda$359(CampaignItem campaignItem, boolean z, boolean z2, boolean z3, boolean z4, Modifier modifier, Function0 function0, Function0 function02, Function0 function03, Function0 function04, Function0 function05, Function0 function06, int i, int i2, int i3, Composer composer, int i4) {
        CampaignCard(campaignItem, z, z2, z3, z4, modifier, function0, function02, function03, function04, function05, function06, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), RecomposeScopeImplKt.updateChangedFlags(i2), i3);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$918(GroupCampaignRepository groupCampaignRepository, String str, Function0 function0, Function7 function7, Function0 function02, boolean z, Function0 function03, int i, int i2, Composer composer, int i3) {
        CreateGroupZikrScreen(groupCampaignRepository, str, function0, function7, function02, z, function03, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit DeleteGroupConfirmDialog$lambda$1055(String str, boolean z, Function0 function0, Function0 function02, int i, int i2, Composer composer, int i3) {
        DeleteGroupConfirmDialog(str, z, function0, function02, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit DhikrPickerSheet$lambda$782(Function0 function0, Function1 function1, boolean z, int i, int i2, Composer composer, int i3) {
        DhikrPickerSheet(function0, function1, z, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit FazilatModalSheet$lambda$1110(String str, Function0 function0, int i, Composer composer, int i2) {
        FazilatModalSheet(str, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GrandUmrahMegaPoster$lambda$308(CampaignItem campaignItem, boolean z, Function0 function0, Function0 function02, Function0 function03, int i, int i2, Composer composer, int i3) {
        GrandUmrahMegaPoster(campaignItem, z, function0, function02, function03, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GroupShareModal$lambda$653(CampaignItem campaignItem, boolean z, boolean z2, String str, Function0 function0, int i, int i2, Composer composer, int i3) {
        GroupShareModal(campaignItem, z, z2, str, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit JoinByCodeDialog$lambda$755(Function0 function0, Function1 function1, int i, Composer composer, int i2) {
        JoinByCodeDialog(function0, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LeaderboardAdPromptDialog$lambda$1043(String str, Function0 function0, Function0 function02, int i, Composer composer, int i2) {
        LeaderboardAdPromptDialog(str, function0, function02, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LeaderboardModalSheet$lambda$1105(CampaignItem campaignItem, String str, GroupCampaignRepository groupCampaignRepository, Function0 function0, int i, int i2, Composer composer, int i3) {
        LeaderboardModalSheet(campaignItem, str, groupCampaignRepository, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$611(CampaignItem campaignItem, GroupCampaignRepository groupCampaignRepository, String str, Function0 function0, Function0 function02, Function0 function03, boolean z, Function0 function04, boolean z2, Function0 function05, int i, int i2, Composer composer, int i3) {
        LiveActiveDhikrRoom(campaignItem, groupCampaignRepository, str, function0, function02, function03, z, function04, z2, function05, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$279(String str, Function0 function0, Modifier modifier, int i, int i2, Composer composer, int i3) {
        LiveDhikrScreen(str, function0, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$94(String str, Function0 function0, Modifier modifier, int i, int i2, Composer composer, int i3) {
        LiveDhikrScreen(str, function0, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LocationSelectionDialog$lambda$1030(String str, List list, Function1 function1, Function0 function0, int i, Composer composer, int i2) {
        LocationSelectionDialog(str, list, function1, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit NoInternetConnectionDialog$lambda$730(Function0 function0, Function0 function02, int i, Composer composer, int i2) {
        NoInternetConnectionDialog(function0, function02, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PillButton$lambda$665(String str, String str2, ImageVector imageVector, boolean z, boolean z2, Function0 function0, int i, int i2, Composer composer, int i3) {
        PillButton(str, str2, imageVector, z, z2, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PulsingLiveDot_Iv8Zu3U$lambda$667(long j, int i, int i2, Composer composer, int i3) {
        m0PulsingLiveDotIv8Zu3U(j, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit ShareIconBtn$lambda$660(String str, Function0 function0, int i, Composer composer, int i2) {
        ShareIconBtn(str, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SharePlatformTile_FU0evQE$lambda$658(String str, String str2, long j, Brush brush, Modifier modifier, Function0 function0, int i, int i2, Composer composer, int i3) {
        m1SharePlatformTileFU0evQE(str, str2, j, brush, modifier, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SmartAutoCounterModal$lambda$743(boolean z, long j, Function0 function0, Function1 function1, Function0 function02, int i, Composer composer, int i2) {
        SmartAutoCounterModal(z, j, function0, function1, function02, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UmrahMegaRegisterModal$lambda$727(CampaignItem campaignItem, GroupCampaignRepository groupCampaignRepository, boolean z, Function0 function0, Function1 function1, int i, int i2, Composer composer, int i3) {
        UmrahMegaRegisterModal(campaignItem, groupCampaignRepository, z, function0, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$1011(GroupCampaignRepository groupCampaignRepository, Function0 function0, int i, Composer composer, int i2) {
        WelcomeToGroupCampaignsDialog(groupCampaignRepository, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    private static final Activity findActivity(Context $this$findActivity) {
        Context ctx = $this$findActivity;
        while (ctx instanceof ContextWrapper) {
            if (ctx instanceof Activity) {
                return (Activity) ctx;
            }
            Context baseContext = ((ContextWrapper) ctx).getBaseContext();
            Intrinsics.checkNotNullExpressionValue(baseContext, "getBaseContext(...)");
            ctx = baseContext;
        }
        return null;
    }

    @Composable
    @Nullable
    public static final ImageBitmap rememberAssetBitmap(@NotNull String path, @Nullable Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter(path, "path");
        ComposerKt.sourceInformationMarkerStart($composer, -8556773, "C(rememberAssetBitmap)210@10059L7,211@10078L232:LiveDhikrScreen.kt#50dkb0");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(-8556773, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.rememberAssetBitmap (LiveDhikrScreen.kt:209)");
        }
        CompositionLocal localContext = AndroidCompositionLocals_androidKt.getLocalContext();
        ComposerKt.sourceInformationMarkerStart($composer, 2023513938, "CC:CompositionLocal.kt#9igjgp");
        Object consume = $composer.consume(localContext);
        ComposerKt.sourceInformationMarkerEnd($composer);
        Context context = (Context) consume;
        ComposerKt.sourceInformationMarkerStart($composer, 366879683, "CC(remember):LiveDhikrScreen.kt#9igjgp");
        boolean z = ((($changed & 14) ^ 6) > 4 && $composer.changed(path)) || ($changed & 6) == 4;
        Object rememberedValue = $composer.rememberedValue();
        if (z || rememberedValue == Composer.Companion.getEmpty()) {
            obj = null;
            try {
                InputStream open = context.getAssets().open(path);
                try {
                    Bitmap decodeStream = BitmapFactory.decodeStream(open);
                    ImageBitmap asImageBitmap = decodeStream != null ? AndroidImageBitmap_androidKt.asImageBitmap(decodeStream) : null;
                    CloseableKt.closeFinally(open, (Throwable) null);
                    obj = asImageBitmap;
                } finally {
                }
            } catch (Exception e) {
            }
            $composer.updateRememberedValue(obj);
        } else {
            obj = rememberedValue;
        }
        ImageBitmap imageBitmap = (ImageBitmap) obj;
        ComposerKt.sourceInformationMarkerEnd($composer);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        ComposerKt.sourceInformationMarkerEnd($composer);
        return imageBitmap;
    }

    /* JADX WARN: Removed duplicated region for block: B:142:0x084e  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x086f  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x08e0  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0927  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x09bd  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x09f6  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0a2f  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0a68  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0aa0  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x0aa9  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0acb  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0a7a  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0a41  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0a08  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x09cf  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x0adc  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0d8d  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x0e04  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0e79  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0e85  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x0ebc  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0fab  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0fb7  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x0fee  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x1169  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x11e6  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x1216  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x1295  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x1349  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x1439  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x1502  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x158a  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x1659  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x168d  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x16d6  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x17aa A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:336:0x1889  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x194b  */
    /* JADX WARN: Removed duplicated region for block: B:355:0x19bf  */
    /* JADX WARN: Removed duplicated region for block: B:358:0x1a20  */
    /* JADX WARN: Removed duplicated region for block: B:361:0x1a84  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x1b03  */
    /* JADX WARN: Removed duplicated region for block: B:372:0x1b6b  */
    /* JADX WARN: Removed duplicated region for block: B:401:0x1d09  */
    /* JADX WARN: Removed duplicated region for block: B:413:0x1a90  */
    /* JADX WARN: Removed duplicated region for block: B:418:0x1a2e  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x19c9  */
    /* JADX WARN: Removed duplicated region for block: B:429:0x19a8  */
    /* JADX WARN: Removed duplicated region for block: B:432:0x1932  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x178b  */
    /* JADX WARN: Removed duplicated region for block: B:440:0x169b  */
    /* JADX WARN: Removed duplicated region for block: B:441:0x1667  */
    /* JADX WARN: Removed duplicated region for block: B:445:0x16b4  */
    /* JADX WARN: Removed duplicated region for block: B:448:0x1579  */
    /* JADX WARN: Removed duplicated region for block: B:449:0x144e  */
    /* JADX WARN: Removed duplicated region for block: B:456:0x14d1  */
    /* JADX WARN: Removed duplicated region for block: B:458:0x14e3  */
    /* JADX WARN: Removed duplicated region for block: B:461:0x1358  */
    /* JADX WARN: Removed duplicated region for block: B:468:0x1408  */
    /* JADX WARN: Removed duplicated region for block: B:470:0x1416  */
    /* JADX WARN: Removed duplicated region for block: B:474:0x12c0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:475:0x1224  */
    /* JADX WARN: Removed duplicated region for block: B:476:0x11f4  */
    /* JADX WARN: Removed duplicated region for block: B:477:0x11bd  */
    /* JADX WARN: Removed duplicated region for block: B:479:0x1004  */
    /* JADX WARN: Removed duplicated region for block: B:480:0x0fbd  */
    /* JADX WARN: Removed duplicated region for block: B:482:0x0ed2  */
    /* JADX WARN: Removed duplicated region for block: B:483:0x0e8b  */
    /* JADX WARN: Removed duplicated region for block: B:484:0x0e10  */
    /* JADX WARN: Removed duplicated region for block: B:487:0x0da1  */
    /* JADX WARN: Removed duplicated region for block: B:489:0x0da6  */
    /* JADX WARN: Removed duplicated region for block: B:514:0x08e2  */
    /* JADX WARN: Removed duplicated region for block: B:516:0x0871  */
    /* JADX WARN: Removed duplicated region for block: B:517:0x085b  */
    /* JADX WARN: Removed duplicated region for block: B:523:0x0730  */
    /* JADX WARN: Removed duplicated region for block: B:535:0x075e  */
    /* JADX WARN: Removed duplicated region for block: B:556:0x0757 A[SYNTHETIC] */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void LiveDhikrScreen(@org.jetbrains.annotations.Nullable java.lang.String r141, @org.jetbrains.annotations.NotNull final kotlin.jvm.functions.Function0<kotlin.Unit> r142, @org.jetbrains.annotations.Nullable androidx.compose.ui.Modifier r143, @org.jetbrains.annotations.Nullable androidx.compose.runtime.Composer r144, final int r145, final int r146) {
        /*
            Method dump skipped, instructions count: 7486
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LiveDhikrScreen(java.lang.String, kotlin.jvm.functions.Function0, androidx.compose.ui.Modifier, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean LiveDhikrScreen$lambda$4(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveDhikrScreen$lambda$5(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final String LiveDhikrScreen$lambda$7(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final boolean LiveDhikrScreen$lambda$10(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveDhikrScreen$lambda$11(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String LiveDhikrScreen$lambda$13(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List<CampaignItem> LiveDhikrScreen$lambda$16(MutableState<List<CampaignItem>> mutableState) {
        return (List) ((State) mutableState).getValue();
    }

    private static final List<CampaignItem> LiveDhikrScreen$lambda$19(MutableState<List<CampaignItem>> mutableState) {
        return (List) ((State) mutableState).getValue();
    }

    private static final int LiveDhikrScreen$lambda$22(MutableIntState $liveCount$delegate) {
        return ((IntState) $liveCount$delegate).getIntValue();
    }

    private static final int LiveDhikrScreen$lambda$25(MutableIntState $completedCount$delegate) {
        return ((IntState) $completedCount$delegate).getIntValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Set<String> LiveDhikrScreen$lambda$28(MutableState<Set<String>> mutableState) {
        return (Set) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Set<String> LiveDhikrScreen$lambda$31(MutableState<Set<String>> mutableState) {
        return (Set) ((State) mutableState).getValue();
    }

    private static final boolean LiveDhikrScreen$lambda$34(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void LiveDhikrScreen$lambda$35(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final CampaignItem LiveDhikrScreen$lambda$37(MutableState<CampaignItem> mutableState) {
        return (CampaignItem) ((State) mutableState).getValue();
    }

    private static final boolean LiveDhikrScreen$lambda$40(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveDhikrScreen$lambda$41(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean LiveDhikrScreen$lambda$43(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveDhikrScreen$lambda$44(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean LiveDhikrScreen$lambda$46(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveDhikrScreen$lambda$47(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean LiveDhikrScreen$lambda$49(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveDhikrScreen$lambda$50(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean LiveDhikrScreen$lambda$52(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void LiveDhikrScreen$lambda$53(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final CampaignItem LiveDhikrScreen$lambda$55(MutableState<CampaignItem> mutableState) {
        return (CampaignItem) ((State) mutableState).getValue();
    }

    private static final CampaignItem LiveDhikrScreen$lambda$58(MutableState<CampaignItem> mutableState) {
        return (CampaignItem) ((State) mutableState).getValue();
    }

    private static final String LiveDhikrScreen$lambda$61(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final CampaignItem LiveDhikrScreen$lambda$64(MutableState<CampaignItem> mutableState) {
        return (CampaignItem) ((State) mutableState).getValue();
    }

    private static final Pair<CampaignItem, Boolean> LiveDhikrScreen$lambda$67(MutableState<Pair<CampaignItem, Boolean>> mutableState) {
        return (Pair) ((State) mutableState).getValue();
    }

    private static final CampaignItem LiveDhikrScreen$lambda$70(MutableState<CampaignItem> mutableState) {
        return (CampaignItem) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void LiveDhikrScreen$reloadData(CoroutineScope coroutineScope, GroupCampaignRepository repository, MutableState<Boolean> mutableState, MutableState<Set<String>> mutableState2, MutableState<Set<String>> mutableState3, MutableState<List<CampaignItem>> mutableState4, MutableState<List<CampaignItem>> mutableState5, MutableIntState liveCount$delegate, MutableIntState completedCount$delegate) {
        BuildersKt.launch$default(coroutineScope, (CoroutineContext) null, (CoroutineStart) null, new LiveDhikrScreen.reloadData.1(repository, mutableState, mutableState2, mutableState3, mutableState4, mutableState5, liveCount$delegate, completedCount$delegate, (Continuation) null), 3, (Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String LiveDhikrScreen$lambda$79(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$83$lambda$82(MutableState $activeCampaign$delegate, CoroutineScope $coroutineScope, GroupCampaignRepository $repository, MutableState $isLoading$delegate, MutableState $joinedIds$delegate, MutableState $savedIds$delegate, MutableState $campaigns$delegate, MutableState $specialCampaigns$delegate, MutableIntState $liveCount$delegate, MutableIntState $completedCount$delegate) {
        $activeCampaign$delegate.setValue(null);
        LiveDhikrScreen$reloadData($coroutineScope, $repository, $isLoading$delegate, $joinedIds$delegate, $savedIds$delegate, $campaigns$delegate, $specialCampaigns$delegate, $liveCount$delegate, $completedCount$delegate);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$93$lambda$92(MutableState $createdShareCampaign$delegate) {
        $createdShareCampaign$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$85$lambda$84(MutableState $activeCampaign$delegate, CoroutineScope $coroutineScope, GroupCampaignRepository $repository, MutableState $isLoading$delegate, MutableState $joinedIds$delegate, MutableState $savedIds$delegate, MutableState $campaigns$delegate, MutableState $specialCampaigns$delegate, MutableIntState $liveCount$delegate, MutableIntState $completedCount$delegate) {
        $activeCampaign$delegate.setValue(null);
        LiveDhikrScreen$reloadData($coroutineScope, $repository, $isLoading$delegate, $joinedIds$delegate, $savedIds$delegate, $campaigns$delegate, $specialCampaigns$delegate, $liveCount$delegate, $completedCount$delegate);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$87$lambda$86(MutableState $activeCampaign$delegate, MutableState $showLeaderboardCampaign$delegate) {
        $showLeaderboardCampaign$delegate.setValue(LiveDhikrScreen$lambda$37($activeCampaign$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$89$lambda$88(MutableState $activeCampaign$delegate, MutableState $showFazilatModal$delegate) {
        CampaignItem LiveDhikrScreen$lambda$37 = LiveDhikrScreen$lambda$37($activeCampaign$delegate);
        Intrinsics.checkNotNull(LiveDhikrScreen$lambda$37);
        $showFazilatModal$delegate.setValue(LiveDhikrScreen$lambda$37.getTitle());
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$91$lambda$90(MutableState $isDarkTheme$delegate) {
        LiveDhikrScreen$lambda$5($isDarkTheme$delegate, !LiveDhikrScreen$lambda$4($isDarkTheme$delegate));
        PrayerRepository.INSTANCE.setDarkTheme(LiveDhikrScreen$lambda$4($isDarkTheme$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean LiveDhikrScreen$lambda$96(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void LiveDhikrScreen$lambda$97(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int LiveDhikrScreen$lambda$99(MutableIntState $mainTourStep$delegate) {
        return ((IntState) $mainTourStep$delegate).getIntValue();
    }

    private static final LayoutCoordinates LiveDhikrScreen$lambda$102(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates LiveDhikrScreen$lambda$105(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates LiveDhikrScreen$lambda$108(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates LiveDhikrScreen$lambda$111(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates LiveDhikrScreen$lambda$114(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$120$lambda$119(MutableState $rootCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $rootCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0b14  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0b79  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0ba0  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0bce  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0c56  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0c62  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0c99  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0cfd  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0d7f  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0d00  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0cad A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0c68  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0be0  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0ba5  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0b7e  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0b17  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0ab9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0a70  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x09e6  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0995  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x08d9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0890  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x07fe  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x079b  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0722 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:150:0x06d9  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x059b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0552  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x03da A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0391  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0213  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x021f  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x037f  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x038b  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x03c4  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0540  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x054c  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0585  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x06c7  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x06d3  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x070c  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0796  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x07e8  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x087e  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x088a  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x08c3  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0990  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x09d4  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0a5e  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0a6a  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0aa3  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$135(kotlin.jvm.functions.Function0 r114, java.lang.String r115, final androidx.compose.runtime.MutableState r116, final androidx.compose.runtime.MutableIntState r117, final androidx.compose.runtime.MutableState r118, final androidx.compose.runtime.MutableState r119, androidx.compose.runtime.Composer r120, int r121) {
        /*
            Method dump skipped, instructions count: 3461
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$135(kotlin.jvm.functions.Function0, java.lang.String, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableIntState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$135$lambda$134$lambda$133$lambda$125$lambda$124(MutableIntState $mainTourStep$delegate, MutableState $isMainTourOpen$delegate) {
        $mainTourStep$delegate.setIntValue(1);
        LiveDhikrScreen$lambda$97($isMainTourOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$135$lambda$134$lambda$133$lambda$128$lambda$127(MutableState $showProfileModal$delegate) {
        LiveDhikrScreen$lambda$53($showProfileModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$135$lambda$134$lambda$133$lambda$131$lambda$130(MutableState $isDarkTheme$delegate) {
        LiveDhikrScreen$lambda$5($isDarkTheme$delegate, !LiveDhikrScreen$lambda$4($isDarkTheme$delegate));
        PrayerRepository.INSTANCE.setDarkTheme(LiveDhikrScreen$lambda$4($isDarkTheme$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$138$lambda$137(MutableState $selectedLanguage$delegate, String code) {
        Intrinsics.checkNotNullParameter(code, "code");
        $selectedLanguage$delegate.setValue(code);
        PrayerRepository.INSTANCE.saveLanguage(code);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$140$lambda$139(MutableState $isMoreLangOpen$delegate) {
        LiveDhikrScreen$lambda$11($isMoreLangOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:114:0x024f  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x02a6  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x02a9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x02b7  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x02f9  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x02fc A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220(final com.tasbih.digitaltasbih.data.groups.CampaignItem r21, final androidx.compose.runtime.MutableState r22, final androidx.compose.runtime.MutableState r23, final androidx.compose.runtime.MutableIntState r24, final androidx.compose.runtime.MutableIntState r25, final androidx.compose.runtime.MutableState r26, final androidx.compose.runtime.MutableState r27, final androidx.compose.runtime.MutableState r28, final androidx.compose.runtime.MutableState r29, final androidx.compose.runtime.MutableState r30, final android.content.Context r31, final com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository r32, final androidx.compose.runtime.MutableState r33, final androidx.compose.runtime.MutableState r34, final androidx.compose.runtime.MutableState r35, final androidx.compose.runtime.MutableState r36, androidx.compose.runtime.MutableState r37, androidx.compose.runtime.MutableState r38, androidx.compose.runtime.MutableState r39, androidx.compose.runtime.MutableState r40, androidx.compose.runtime.MutableState r41, kotlinx.coroutines.CoroutineScope r42, androidx.compose.runtime.MutableState r43, androidx.compose.runtime.MutableState r44, androidx.compose.runtime.MutableState r45, androidx.compose.foundation.lazy.LazyListScope r46) {
        /*
            Method dump skipped, instructions count: 938
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220(com.tasbih.digitaltasbih.data.groups.CampaignItem, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableIntState, androidx.compose.runtime.MutableIntState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, android.content.Context, com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, kotlinx.coroutines.CoroutineScope, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.foundation.lazy.LazyListScope):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0b79  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0cde  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0b8f  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0b48  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0a66  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x09de A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0995  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x08be  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0875  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x07e6  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0606 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x05bd  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x04dc  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0452 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0409  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x02eb  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x024f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0241  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x02d9  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x02e5  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x03f7  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0403  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x043c  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x04a3  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x05ab  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x05b7  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x05f0  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x07da  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0863  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x086f  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x08a8  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0983  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x098f  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x09c8  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0a2d  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0b36  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0b42  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$154(final androidx.compose.runtime.MutableState r149, final androidx.compose.runtime.MutableState r150, androidx.compose.runtime.MutableIntState r151, androidx.compose.runtime.MutableIntState r152, androidx.compose.foundation.lazy.LazyItemScope r153, androidx.compose.runtime.Composer r154, int r155) {
        /*
            Method dump skipped, instructions count: 3300
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$154(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableIntState, androidx.compose.runtime.MutableIntState, androidx.compose.foundation.lazy.LazyItemScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$154$lambda$142$lambda$141(MutableState $statsRowCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $statsRowCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$154$lambda$153$lambda$144$lambda$143(MutableState $selectedTab$delegate) {
        $selectedTab$delegate.setValue("public");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$154$lambda$153$lambda$149$lambda$148(MutableState $selectedTab$delegate) {
        $selectedTab$delegate.setValue("history");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176(final CampaignItem $activeSpecialCampaign, final MutableState $tabsRowCoords$delegate, final MutableState $selectedTab$delegate, final MutableState $isDarkTheme$delegate, final MutableState $showCreateModal$delegate, LazyItemScope $this$item, Composer $composer, int $changed) {
        Object obj;
        Object obj2;
        Intrinsics.checkNotNullParameter($this$item, "$this$item");
        ComposerKt.sourceInformation($composer, "C700@31292L22,702@31405L3512,697@31157L3760:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-356587289, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:697)");
            }
            Modifier fillMaxWidth$default = SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, (Object) null);
            ComposerKt.sourceInformationMarkerStart($composer, 917003869, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function1() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda242
                    public final Object invoke(Object obj3) {
                        return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$156$lambda$155($tabsRowCoords$delegate, (LayoutCoordinates) obj3);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            Modifier onGloballyPositioned = OnGloballyPositionedModifierKt.onGloballyPositioned(fillMaxWidth$default, (Function1) obj);
            Arrangement.Horizontal horizontal = Arrangement.INSTANCE.spacedBy-0680j_4(Dp.constructor-impl(8));
            ComposerKt.sourceInformationMarkerStart($composer, 917010975, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            boolean changed = $composer.changed($activeSpecialCampaign);
            Object rememberedValue2 = $composer.rememberedValue();
            if (changed || rememberedValue2 == Composer.Companion.getEmpty()) {
                obj2 = new Function1() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda243
                    public final Object invoke(Object obj3) {
                        return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174($activeSpecialCampaign, $selectedTab$delegate, $isDarkTheme$delegate, $showCreateModal$delegate, (LazyListScope) obj3);
                    }
                };
                $composer.updateRememberedValue(obj2);
            } else {
                obj2 = rememberedValue2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            LazyDslKt.LazyRow(onGloballyPositioned, (LazyListState) null, (PaddingValues) null, false, horizontal, (Alignment.Vertical) null, (FlingBehavior) null, false, (Function1) obj2, $composer, 24582, 238);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$156$lambda$155(MutableState $tabsRowCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $tabsRowCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174(CampaignItem $activeSpecialCampaign, final MutableState $selectedTab$delegate, final MutableState $isDarkTheme$delegate, final MutableState $showCreateModal$delegate, LazyListScope $this$LazyRow) {
        Intrinsics.checkNotNullParameter($this$LazyRow, "$this$LazyRow");
        LazyListScope.item$default($this$LazyRow, (Object) null, (Object) null, ComposableLambdaKt.composableLambdaInstance(-237291899, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda262
            public final Object invoke(Object obj, Object obj2, Object obj3) {
                return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$159($selectedTab$delegate, $isDarkTheme$delegate, $showCreateModal$delegate, (LazyItemScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
            }
        }), 3, (Object) null);
        LazyListScope.item$default($this$LazyRow, (Object) null, (Object) null, ComposableLambdaKt.composableLambdaInstance(113970158, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda263
            public final Object invoke(Object obj, Object obj2, Object obj3) {
                return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$162($selectedTab$delegate, $isDarkTheme$delegate, (LazyItemScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
            }
        }), 3, (Object) null);
        LazyListScope.item$default($this$LazyRow, (Object) null, (Object) null, ComposableLambdaKt.composableLambdaInstance(-576933811, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda264
            public final Object invoke(Object obj, Object obj2, Object obj3) {
                return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$165($selectedTab$delegate, $isDarkTheme$delegate, (LazyItemScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
            }
        }), 3, (Object) null);
        LazyListScope.item$default($this$LazyRow, (Object) null, (Object) null, ComposableLambdaKt.composableLambdaInstance(-1267837780, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda265
            public final Object invoke(Object obj, Object obj2, Object obj3) {
                return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$168($selectedTab$delegate, $isDarkTheme$delegate, (LazyItemScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
            }
        }), 3, (Object) null);
        if ($activeSpecialCampaign != null) {
            LazyListScope.item$default($this$LazyRow, (Object) null, (Object) null, ComposableLambdaKt.composableLambdaInstance(-77283510, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda266
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$173($selectedTab$delegate, (LazyItemScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }), 3, (Object) null);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$159(MutableState $selectedTab$delegate, MutableState $isDarkTheme$delegate, final MutableState $showCreateModal$delegate, LazyItemScope $this$item, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$item, "$this$item");
        ComposerKt.sourceInformation($composer, "C710@31806L26,704@31458L400:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-237291899, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:704)");
            }
            ImageVector add = AddKt.getAdd(Icons.Filled.INSTANCE);
            boolean areEqual = Intrinsics.areEqual(LiveDhikrScreen$lambda$13($selectedTab$delegate), "create");
            boolean LiveDhikrScreen$lambda$4 = LiveDhikrScreen$lambda$4($isDarkTheme$delegate);
            ComposerKt.sourceInformationMarkerStart($composer, -404236257, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda0
                    public final Object invoke() {
                        return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$159$lambda$158$lambda$157($showCreateModal$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            PillButton("Create", "media/8a5a1316bff8e274.webp", add, areEqual, LiveDhikrScreen$lambda$4, (Function0) obj, $composer, 196662, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$159$lambda$158$lambda$157(MutableState $showCreateModal$delegate) {
        LiveDhikrScreen$lambda$41($showCreateModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$162(final MutableState $selectedTab$delegate, MutableState $isDarkTheme$delegate, LazyItemScope $this$item, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$item, "$this$item");
        ComposerKt.sourceInformation($composer, "C720@32284L24,714@31932L402:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(113970158, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:714)");
            }
            ImageVector groups = GroupsKt.getGroups(Icons.Filled.INSTANCE);
            boolean areEqual = Intrinsics.areEqual(LiveDhikrScreen$lambda$13($selectedTab$delegate), "mine");
            boolean LiveDhikrScreen$lambda$4 = LiveDhikrScreen$lambda$4($isDarkTheme$delegate);
            ComposerKt.sourceInformationMarkerStart($composer, -2143815738, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda166
                    public final Object invoke() {
                        return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$162$lambda$161$lambda$160($selectedTab$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            PillButton("My Groups", "media/2de406854aaa71f2.webp", groups, areEqual, LiveDhikrScreen$lambda$4, (Function0) obj, $composer, 196662, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$162$lambda$161$lambda$160(MutableState $selectedTab$delegate) {
        $selectedTab$delegate.setValue("mine");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$165(final MutableState $selectedTab$delegate, MutableState $isDarkTheme$delegate, LazyItemScope $this$item, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$item, "$this$item");
        ComposerKt.sourceInformation($composer, "C730@32734L25,724@32408L377:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-576933811, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:724)");
            }
            ImageVector bookmark = BookmarkKt.getBookmark(Icons.Filled.INSTANCE);
            boolean areEqual = Intrinsics.areEqual(LiveDhikrScreen$lambda$13($selectedTab$delegate), "saved");
            boolean LiveDhikrScreen$lambda$4 = LiveDhikrScreen$lambda$4($isDarkTheme$delegate);
            ComposerKt.sourceInformationMarkerStart($composer, -382599898, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda38
                    public final Object invoke() {
                        return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$165$lambda$164$lambda$163($selectedTab$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            PillButton("Saved", null, bookmark, areEqual, LiveDhikrScreen$lambda$4, (Function0) obj, $composer, 196662, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$165$lambda$164$lambda$163(MutableState $selectedTab$delegate) {
        $selectedTab$delegate.setValue("saved");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$168(final MutableState $selectedTab$delegate, MutableState $isDarkTheme$delegate, LazyItemScope $this$item, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$item, "$this$item");
        ComposerKt.sourceInformation($composer, "C740@33213L27,734@32859L407:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1267837780, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:734)");
            }
            ImageVector history = HistoryKt.getHistory(Icons.Filled.INSTANCE);
            boolean areEqual = Intrinsics.areEqual(LiveDhikrScreen$lambda$13($selectedTab$delegate), "history");
            boolean LiveDhikrScreen$lambda$4 = LiveDhikrScreen$lambda$4($isDarkTheme$delegate);
            ComposerKt.sourceInformationMarkerStart($composer, 1378617671, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda68
                    public final Object invoke() {
                        return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$168$lambda$167$lambda$166($selectedTab$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            PillButton("History", "media/80b45c4747e66808.webp", history, areEqual, LiveDhikrScreen$lambda$4, (Function0) obj, $composer, 196662, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$168$lambda$167$lambda$166(MutableState $selectedTab$delegate) {
        $selectedTab$delegate.setValue("history");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0274  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0280  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x02b7  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x03ba  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x02cd  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0286  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$173(final androidx.compose.runtime.MutableState r73, androidx.compose.foundation.lazy.LazyItemScope r74, androidx.compose.runtime.Composer r75, int r76) {
        /*
            Method dump skipped, instructions count: 960
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$173(androidx.compose.runtime.MutableState, androidx.compose.foundation.lazy.LazyItemScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$176$lambda$175$lambda$174$lambda$173$lambda$170$lambda$169(MutableState $selectedTab$delegate) {
        $selectedTab$delegate.setValue("special");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0c7d  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0c89  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0cc2  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0d27  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0e30  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0e3c  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0e75  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x1009  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x1015  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x104c  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x113d  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x1062 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x101b  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0e8b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0e42  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0d60  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0cd8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0c8f  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0bb4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0b6b  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0ad0  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x096a  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0921  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0746 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x06fd  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0622 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:162:0x05d9  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x04f8  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0470 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0427  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x02fe  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x02ec  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x02f8  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0415  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0421  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x045a  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x04c1  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x05c7  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x05d3  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x060c  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x06eb  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x06f7  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0730  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x090f  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x091b  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0954  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0ac2  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0b59  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0b65  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0b9e  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$193(final androidx.compose.runtime.MutableState r164, final androidx.compose.runtime.MutableState r165, final androidx.compose.runtime.MutableState r166, androidx.compose.foundation.lazy.LazyItemScope r167, androidx.compose.runtime.Composer r168, int r169) {
        /*
            Method dump skipped, instructions count: 4419
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$193(androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.foundation.lazy.LazyItemScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$193$lambda$178$lambda$177(MutableState $actionRowCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $actionRowCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$193$lambda$192$lambda$180$lambda$179(MutableState $selectedTab$delegate) {
        $selectedTab$delegate.setValue("public");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$193$lambda$192$lambda$187$lambda$186(MutableState $showJoinCodeModal$delegate) {
        LiveDhikrScreen$lambda$44($showJoinCodeModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$200(final CampaignItem $activeSpecialCampaign, final Context $context, final GroupCampaignRepository $repository, MutableState $isDarkTheme$delegate, final MutableState $showNoInternetDialog$delegate, final MutableState $showMegaRegisterModal$delegate, final MutableState $activeCampaign$delegate, final MutableState $pendingAdCampaign$delegate, LazyItemScope $this$item, Composer $composer, int $changed) {
        Object obj;
        final MutableState mutableState;
        Object obj2;
        Function0 function0;
        Object obj3;
        final CampaignItem campaignItem = $activeSpecialCampaign;
        Intrinsics.checkNotNullParameter($this$item, "$this$item");
        ComposerKt.sourceInformation($composer, "C918@42668L652,930@43364L283,937@43694L296,915@42499L1513:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(405381131, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:915)");
            }
            boolean LiveDhikrScreen$lambda$4 = LiveDhikrScreen$lambda$4($isDarkTheme$delegate);
            ComposerKt.sourceInformationMarkerStart($composer, -1270750281, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            boolean changedInstance = $composer.changedInstance($context) | $composer.changedInstance($repository) | $composer.changed(campaignItem);
            Object rememberedValue = $composer.rememberedValue();
            if (changedInstance || rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda34
                    public final Object invoke() {
                        return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$200$lambda$195$lambda$194($context, $repository, $activeSpecialCampaign, $showNoInternetDialog$delegate, $showMegaRegisterModal$delegate, $activeCampaign$delegate);
                    }
                };
                campaignItem = $activeSpecialCampaign;
                mutableState = $showNoInternetDialog$delegate;
                $composer.updateRememberedValue(obj);
            } else {
                mutableState = $showNoInternetDialog$delegate;
                obj = rememberedValue;
            }
            Function0 function02 = (Function0) obj;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerStart($composer, -1270728378, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            boolean changedInstance2 = $composer.changedInstance($context);
            Object rememberedValue2 = $composer.rememberedValue();
            if (changedInstance2 || rememberedValue2 == Composer.Companion.getEmpty()) {
                obj2 = new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda35
                    public final Object invoke() {
                        return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$200$lambda$197$lambda$196($context, mutableState, $showMegaRegisterModal$delegate);
                    }
                };
                $composer.updateRememberedValue(obj2);
            } else {
                obj2 = rememberedValue2;
            }
            Function0 function03 = (Function0) obj2;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerStart($composer, -1270717805, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            boolean changedInstance3 = $composer.changedInstance($context) | $composer.changed(campaignItem);
            Object rememberedValue3 = $composer.rememberedValue();
            if (changedInstance3 || rememberedValue3 == Composer.Companion.getEmpty()) {
                function0 = function02;
                obj3 = new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda36
                    public final Object invoke() {
                        return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$200$lambda$199$lambda$198($context, campaignItem, mutableState, $pendingAdCampaign$delegate);
                    }
                };
                $composer.updateRememberedValue(obj3);
            } else {
                function0 = function02;
                obj3 = rememberedValue3;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            GrandUmrahMegaPoster(campaignItem, LiveDhikrScreen$lambda$4, function0, function03, (Function0) obj3, $composer, 0, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$200$lambda$195$lambda$194(Context $context, GroupCampaignRepository $repository, CampaignItem $activeSpecialCampaign, MutableState $showNoInternetDialog$delegate, MutableState $showMegaRegisterModal$delegate, MutableState $activeCampaign$delegate) {
        if (!NetworkHelper.INSTANCE.isOnline($context)) {
            LiveDhikrScreen$lambda$50($showNoInternetDialog$delegate, true);
        } else {
            StoredMegaTicket existingTicket = $repository.getStoredMegaTicket($activeSpecialCampaign.getCampaignId());
            if (existingTicket == null && !$repository.isConsentAccepted()) {
                LiveDhikrScreen$lambda$47($showMegaRegisterModal$delegate, true);
            } else {
                $activeCampaign$delegate.setValue($activeSpecialCampaign);
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$200$lambda$197$lambda$196(Context $context, MutableState $showNoInternetDialog$delegate, MutableState $showMegaRegisterModal$delegate) {
        if (!NetworkHelper.INSTANCE.isOnline($context)) {
            LiveDhikrScreen$lambda$50($showNoInternetDialog$delegate, true);
        } else {
            LiveDhikrScreen$lambda$47($showMegaRegisterModal$delegate, true);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$200$lambda$199$lambda$198(Context $context, CampaignItem $activeSpecialCampaign, MutableState $showNoInternetDialog$delegate, MutableState $pendingAdCampaign$delegate) {
        if (!NetworkHelper.INSTANCE.isOnline($context)) {
            LiveDhikrScreen$lambda$50($showNoInternetDialog$delegate, true);
        } else {
            $pendingAdCampaign$delegate.setValue($activeSpecialCampaign);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$210(MutableState $selectedTab$delegate, MutableState $isDarkTheme$delegate, LazyItemScope $this$item, Composer $composer, int $changed) {
        String str;
        Intrinsics.checkNotNullParameter($this$item, "$this$item");
        ComposerKt.sourceInformation($composer, "C974@45592L1311:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(267769835, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:974)");
            }
            Modifier modifier = PaddingKt.padding-qDBjuR0$default(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, (Object) null), 0.0f, Dp.constructor-impl(50), 0.0f, Dp.constructor-impl(40), 5, (Object) null);
            Alignment.Horizontal centerHorizontally = Alignment.Companion.getCenterHorizontally();
            ComposerKt.sourceInformationMarkerStart($composer, -483455358, "CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            MeasurePolicy columnMeasurePolicy = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), centerHorizontally, $composer, ((390 >> 3) & 14) | ((390 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer, -1323940314, "CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            int currentCompositeKeyHash = ComposablesKt.getCurrentCompositeKeyHash($composer, 0);
            CompositionLocalMap currentCompositionLocalMap = $composer.getCurrentCompositionLocalMap();
            Modifier materializeModifier = ComposedModifierKt.materializeModifier($composer, modifier);
            Function0 constructor = ComposeUiNode.Companion.getConstructor();
            int i = ((((390 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart($composer, -692256719, "CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!($composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            $composer.startReusableNode();
            if ($composer.getInserting()) {
                $composer.createNode(constructor);
            } else {
                $composer.useNode();
            }
            Composer composer = Updater.constructor-impl($composer);
            Updater.set-impl(composer, columnMeasurePolicy, ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl(composer, currentCompositionLocalMap, ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            Function2 setCompositeKeyHash = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (composer.getInserting() || !Intrinsics.areEqual(composer.rememberedValue(), Integer.valueOf(currentCompositeKeyHash))) {
                composer.updateRememberedValue(Integer.valueOf(currentCompositeKeyHash));
                composer.apply(Integer.valueOf(currentCompositeKeyHash), setCompositeKeyHash);
            }
            Updater.set-impl(composer, materializeModifier, ComposeUiNode.Companion.getSetModifier());
            int i2 = (i >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart($composer, -384784025, "C88@4444L9:Column.kt#2w3rfo");
            ColumnScope columnScope = ColumnScopeInstance.INSTANCE;
            int i3 = ((390 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart($composer, -560406601, "C980@45878L609,991@46512L40,992@46577L304:LiveDhikrScreen.kt#50dkb0");
            String LiveDhikrScreen$lambda$13 = LiveDhikrScreen$lambda$13($selectedTab$delegate);
            switch (LiveDhikrScreen$lambda$13.hashCode()) {
                case 3351635:
                    if (LiveDhikrScreen$lambda$13.equals("mine")) {
                        str = "You haven't joined any groups yet.";
                        break;
                    }
                    str = "No public groups right now.";
                    break;
                case 109211271:
                    if (LiveDhikrScreen$lambda$13.equals("saved")) {
                        str = "No saved groups found.";
                        break;
                    }
                    str = "No public groups right now.";
                    break;
                case 926934164:
                    if (LiveDhikrScreen$lambda$13.equals("history")) {
                        str = "No completed groups yet.";
                        break;
                    }
                    str = "No public groups right now.";
                    break;
                default:
                    str = "No public groups right now.";
                    break;
            }
            TextKt.Text--4IGK_g(str, (Modifier) null, ColorKt.Color(LiveDhikrScreen$lambda$4($isDarkTheme$delegate) ? 4288455599L : 4284114526L), TextUnitKt.getSp(14.5d), (FontStyle) null, FontWeight.Companion.getNormal(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 199680, 0, 131026);
            SpacerKt.Spacer(SizeKt.height-3ABfNKs(Modifier.Companion, Dp.constructor-impl(4)), $composer, 6);
            TextKt.Text--4IGK_g("Be the first to create one! 🤲", (Modifier) null, ColorKt.Color(LiveDhikrScreen$lambda$4($isDarkTheme$delegate) ? 4291941851L : 4281811281L), TextUnitKt.getSp(14.5d), (FontStyle) null, FontWeight.Companion.getSemiBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 199686, 0, 131026);
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerEnd($composer);
            $composer.endNode();
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerEnd($composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Object LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$211(int i, CampaignItem camp) {
        Intrinsics.checkNotNullParameter(camp, "camp");
        return camp.getCampaignId();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$228$lambda$225$lambda$224(Context $context, Activity $activity, final CoroutineScope $coroutineScope, MutableState $campaignToDelete$delegate, final GroupCampaignRepository $repository, final MutableState $activeCampaign$delegate, final MutableState $isLoading$delegate, final MutableState $joinedIds$delegate, final MutableState $savedIds$delegate, final MutableState $campaigns$delegate, final MutableState $specialCampaigns$delegate, final MutableIntState $liveCount$delegate, final MutableIntState $completedCount$delegate) {
        final CampaignItem toDelete = LiveDhikrScreen$lambda$70($campaignToDelete$delegate);
        $campaignToDelete$delegate.setValue(null);
        if (toDelete != null) {
            Activity findActivity = findActivity($context);
            if (findActivity == null) {
                findActivity = $activity;
            }
            Activity act = findActivity;
            if (act != null) {
                AdMobRewardedManager.INSTANCE.showAd(act, "Delete Group: " + toDelete.getTitle(), new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda1
                    public final Object invoke() {
                        return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$228$lambda$225$lambda$224$lambda$223($coroutineScope, $repository, toDelete, $activeCampaign$delegate, $isLoading$delegate, $joinedIds$delegate, $savedIds$delegate, $campaigns$delegate, $specialCampaigns$delegate, $liveCount$delegate, $completedCount$delegate);
                    }
                });
            } else {
                BuildersKt.launch$default($coroutineScope, (CoroutineContext) null, (CoroutineStart) null, new LiveDhikrScreen.14.2.1.1.2($repository, toDelete, $activeCampaign$delegate, $coroutineScope, $isLoading$delegate, $joinedIds$delegate, $savedIds$delegate, $campaigns$delegate, $specialCampaigns$delegate, $liveCount$delegate, $completedCount$delegate, (Continuation) null), 3, (Object) null);
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$228$lambda$225$lambda$224$lambda$223(CoroutineScope $coroutineScope, GroupCampaignRepository $repository, CampaignItem $toDelete, MutableState $activeCampaign$delegate, MutableState $isLoading$delegate, MutableState $joinedIds$delegate, MutableState $savedIds$delegate, MutableState $campaigns$delegate, MutableState $specialCampaigns$delegate, MutableIntState $liveCount$delegate, MutableIntState $completedCount$delegate) {
        BuildersKt.launch$default($coroutineScope, (CoroutineContext) null, (CoroutineStart) null, new LiveDhikrScreen.14.2.1.1.1.1($repository, $toDelete, $activeCampaign$delegate, $coroutineScope, $isLoading$delegate, $joinedIds$delegate, $savedIds$delegate, $campaigns$delegate, $specialCampaigns$delegate, $liveCount$delegate, $completedCount$delegate, (Continuation) null), 3, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$228$lambda$227$lambda$226(MutableState $campaignToDelete$delegate) {
        $campaignToDelete$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$234$lambda$231$lambda$230(Activity $activity, MutableState $pendingAdCampaign$delegate, final MutableState $showLeaderboardCampaign$delegate) {
        final CampaignItem campToOpen = LiveDhikrScreen$lambda$58($pendingAdCampaign$delegate);
        $pendingAdCampaign$delegate.setValue(null);
        if ($activity != null && campToOpen != null) {
            AdMobRewardedManager.INSTANCE.showAd($activity, "Group Reciters Leaderboard", new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda228
                public final Object invoke() {
                    return LiveDhikrScreenKt.LiveDhikrScreen$lambda$278$lambda$234$lambda$231$lambda$230$lambda$229(campToOpen, $showLeaderboardCampaign$delegate);
                }
            });
        } else if (campToOpen != null) {
            $showLeaderboardCampaign$delegate.setValue(campToOpen);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$234$lambda$231$lambda$230$lambda$229(CampaignItem $campToOpen, MutableState $showLeaderboardCampaign$delegate) {
        $showLeaderboardCampaign$delegate.setValue($campToOpen);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$234$lambda$233$lambda$232(MutableState $pendingAdCampaign$delegate) {
        $pendingAdCampaign$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$236$lambda$235(MutableState $selectedLanguage$delegate, MutableState $isMoreLangOpen$delegate, String lang) {
        Intrinsics.checkNotNullParameter(lang, "lang");
        $selectedLanguage$delegate.setValue(lang);
        PrayerRepository.INSTANCE.saveLanguage(lang);
        LiveDhikrScreen$lambda$11($isMoreLangOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$238$lambda$237(MutableState $isMoreLangOpen$delegate) {
        LiveDhikrScreen$lambda$11($isMoreLangOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$240$lambda$239(MutableState $showCreateModal$delegate) {
        LiveDhikrScreen$lambda$41($showCreateModal$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$242$lambda$241(CoroutineScope $coroutineScope, MutableState $showCreateModal$delegate, GroupCampaignRepository $repository, MutableState $isLoading$delegate, MutableState $joinedIds$delegate, MutableState $savedIds$delegate, MutableState $campaigns$delegate, MutableState $specialCampaigns$delegate, MutableIntState $liveCount$delegate, MutableIntState $completedCount$delegate, MutableState $activeCampaign$delegate, MutableState $createdShareCampaign$delegate, String title, String arabic, long target, boolean isPriv, String city, boolean isSpecialMega, String note) {
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(arabic, "arabic");
        Intrinsics.checkNotNullParameter(city, "city");
        Intrinsics.checkNotNullParameter(note, "note");
        LiveDhikrScreen$lambda$41($showCreateModal$delegate, false);
        BuildersKt.launch$default($coroutineScope, (CoroutineContext) null, (CoroutineStart) null, new LiveDhikrScreen.14.7.1.1($repository, title, arabic, target, isPriv, city, note, isSpecialMega, $coroutineScope, $isLoading$delegate, $joinedIds$delegate, $savedIds$delegate, $campaigns$delegate, $specialCampaigns$delegate, $liveCount$delegate, $completedCount$delegate, $activeCampaign$delegate, $createdShareCampaign$delegate, (Continuation) null), 3, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$244$lambda$243(MutableState $showMegaRegisterModal$delegate) {
        LiveDhikrScreen$lambda$47($showMegaRegisterModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$246$lambda$245(MutableState $isDarkTheme$delegate) {
        LiveDhikrScreen$lambda$5($isDarkTheme$delegate, !LiveDhikrScreen$lambda$4($isDarkTheme$delegate));
        PrayerRepository.INSTANCE.setDarkTheme(LiveDhikrScreen$lambda$4($isDarkTheme$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$248$lambda$247(MutableState $showJoinCodeModal$delegate) {
        LiveDhikrScreen$lambda$44($showJoinCodeModal$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$250$lambda$249(CoroutineScope $coroutineScope, MutableState $showJoinCodeModal$delegate, GroupCampaignRepository $repository, MutableState $joinedIds$delegate, MutableState $campaigns$delegate, MutableState $isLoading$delegate, MutableState $savedIds$delegate, MutableState $specialCampaigns$delegate, MutableIntState $liveCount$delegate, MutableIntState $completedCount$delegate, MutableState $activeCampaign$delegate, String code) {
        Intrinsics.checkNotNullParameter(code, "code");
        LiveDhikrScreen$lambda$44($showJoinCodeModal$delegate, false);
        BuildersKt.launch$default($coroutineScope, (CoroutineContext) null, (CoroutineStart) null, new LiveDhikrScreen.14.11.1.1($repository, code, $joinedIds$delegate, $campaigns$delegate, $coroutineScope, $isLoading$delegate, $savedIds$delegate, $specialCampaigns$delegate, $liveCount$delegate, $completedCount$delegate, $activeCampaign$delegate, (Continuation) null), 3, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$252$lambda$251(MutableState $showMegaRegisterModal$delegate) {
        LiveDhikrScreen$lambda$47($showMegaRegisterModal$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$254$lambda$253(CampaignItem $activeSpecialCampaign, MutableState $showMegaRegisterModal$delegate, CoroutineScope $coroutineScope, GroupCampaignRepository $repository, MutableState $isLoading$delegate, MutableState $joinedIds$delegate, MutableState $savedIds$delegate, MutableState $campaigns$delegate, MutableState $specialCampaigns$delegate, MutableIntState $liveCount$delegate, MutableIntState $completedCount$delegate, MutableState $activeCampaign$delegate, StoredMegaTicket ticket) {
        Intrinsics.checkNotNullParameter(ticket, "ticket");
        LiveDhikrScreen$lambda$47($showMegaRegisterModal$delegate, false);
        LiveDhikrScreen$reloadData($coroutineScope, $repository, $isLoading$delegate, $joinedIds$delegate, $savedIds$delegate, $campaigns$delegate, $specialCampaigns$delegate, $liveCount$delegate, $completedCount$delegate);
        $activeCampaign$delegate.setValue($activeSpecialCampaign);
        AppToast.INSTANCE.show("🎉 Mubarak! Registered Ticket #" + ticket.getTicketNumber());
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$256$lambda$255(MutableState $showNoInternetDialog$delegate) {
        LiveDhikrScreen$lambda$50($showNoInternetDialog$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$258$lambda$257(Context $context, MutableState $showNoInternetDialog$delegate, CoroutineScope $coroutineScope, GroupCampaignRepository $repository, MutableState $isLoading$delegate, MutableState $joinedIds$delegate, MutableState $savedIds$delegate, MutableState $campaigns$delegate, MutableState $specialCampaigns$delegate, MutableIntState $liveCount$delegate, MutableIntState $completedCount$delegate) {
        if (NetworkHelper.INSTANCE.isOnline($context)) {
            LiveDhikrScreen$lambda$50($showNoInternetDialog$delegate, false);
            LiveDhikrScreen$reloadData($coroutineScope, $repository, $isLoading$delegate, $joinedIds$delegate, $savedIds$delegate, $campaigns$delegate, $specialCampaigns$delegate, $liveCount$delegate, $completedCount$delegate);
            AppToast.INSTANCE.show("Connected to internet!");
        } else {
            AppToast.INSTANCE.show("Still offline. Kindly turn on Wi-Fi or Mobile Data.");
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$260$lambda$259(MutableState $showProfileModal$delegate, CoroutineScope $coroutineScope, GroupCampaignRepository $repository, MutableState $isLoading$delegate, MutableState $joinedIds$delegate, MutableState $savedIds$delegate, MutableState $campaigns$delegate, MutableState $specialCampaigns$delegate, MutableIntState $liveCount$delegate, MutableIntState $completedCount$delegate) {
        LiveDhikrScreen$lambda$53($showProfileModal$delegate, false);
        LiveDhikrScreen$reloadData($coroutineScope, $repository, $isLoading$delegate, $joinedIds$delegate, $savedIds$delegate, $campaigns$delegate, $specialCampaigns$delegate, $liveCount$delegate, $completedCount$delegate);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$263$lambda$262$lambda$261(MutableState $showLeaderboardCampaign$delegate) {
        $showLeaderboardCampaign$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$266$lambda$265$lambda$264(MutableState $showFazilatModal$delegate) {
        $showFazilatModal$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$269$lambda$268$lambda$267(MutableState $shareModalCampaign$delegate) {
        $shareModalCampaign$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$271$lambda$270(MutableState $createdShareCampaign$delegate) {
        $createdShareCampaign$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$273$lambda$272(GroupCampaignRepository $repository, MutableIntState $mainTourStep$delegate, MutableState $isMainTourOpen$delegate) {
        if (LiveDhikrScreen$lambda$99($mainTourStep$delegate) >= 4) {
            $repository.setMainTourDone(true);
            LiveDhikrScreen$lambda$97($isMainTourOpen$delegate, false);
        } else {
            $mainTourStep$delegate.setIntValue(LiveDhikrScreen$lambda$99($mainTourStep$delegate) + 1);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$275$lambda$274(GroupCampaignRepository $repository, MutableState $isMainTourOpen$delegate) {
        $repository.setMainTourDone(true);
        LiveDhikrScreen$lambda$97($isMainTourOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveDhikrScreen$lambda$278$lambda$277$lambda$276(GroupCampaignRepository $repository, MutableState $isMainTourOpen$delegate) {
        $repository.setMainTourDone(true);
        LiveDhikrScreen$lambda$97($isMainTourOpen$delegate, false);
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    private static final void GrandUmrahMegaPoster(final CampaignItem campaign, boolean isDarkTheme, final Function0<Unit> function0, final Function0<Unit> function02, Function0<Unit> function03, Composer $composer, final int $changed, final int i) {
        boolean z;
        Function0 function04;
        boolean isDarkTheme2;
        char c;
        Function0 onViewParticipants;
        Brush brush;
        Object obj;
        Composer $composer2;
        final boolean isDarkTheme3;
        final Function0 onViewParticipants2;
        Object obj2;
        Composer $composer3 = $composer.startRestartGroup(-1553676435);
        ComposerKt.sourceInformation($composer3, "C(GrandUmrahMegaPoster)1309@59286L2,1334@60405L17,1336@60497L35,1337@60567L65,1338@60639L11097,1319@59831L11905:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(campaign) ? 4 : 2;
        }
        int i2 = i & 2;
        if (i2 != 0) {
            $dirty |= 48;
            z = isDarkTheme;
        } else if (($changed & 48) == 0) {
            z = isDarkTheme;
            $dirty |= $composer3.changed(z) ? 32 : 16;
        } else {
            z = isDarkTheme;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer3.changedInstance(function0) ? 256 : 128;
        }
        if (($changed & 3072) == 0) {
            $dirty |= $composer3.changedInstance(function02) ? 2048 : 1024;
        }
        int i3 = i & 16;
        if (i3 != 0) {
            $dirty |= 24576;
            function04 = function03;
        } else if (($changed & 24576) == 0) {
            function04 = function03;
            $dirty |= $composer3.changedInstance(function04) ? 16384 : 8192;
        } else {
            function04 = function03;
        }
        if (($dirty & 9363) == 9362 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            isDarkTheme3 = z;
            $composer2 = $composer3;
            onViewParticipants2 = function04;
        } else {
            if (i2 != 0) {
                isDarkTheme2 = false;
            } else {
                isDarkTheme2 = z;
            }
            if (i3 != 0) {
                ComposerKt.sourceInformationMarkerStart($composer3, 1565571599, "CC(remember):LiveDhikrScreen.kt#9igjgp");
                Object rememberedValue = $composer3.rememberedValue();
                c = 2;
                if (rememberedValue == Composer.Companion.getEmpty()) {
                    obj2 = new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda81
                        public final Object invoke() {
                            Unit unit;
                            unit = Unit.INSTANCE;
                            return unit;
                        }
                    };
                    $composer3.updateRememberedValue(obj2);
                } else {
                    obj2 = rememberedValue;
                }
                Function0 onViewParticipants3 = (Function0) obj2;
                ComposerKt.sourceInformationMarkerEnd($composer3);
                onViewParticipants = onViewParticipants3;
            } else {
                c = 2;
                onViewParticipants = function04;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1553676435, $dirty, -1, "com.tasbih.digitaltasbih.ui.screens.groups.GrandUmrahMegaPoster (LiveDhikrScreen.kt:1310)");
            }
            long cardBg = isDarkTheme2 ? DarkCardBg : Color.Companion.getWhite-0d7_KjU();
            if (isDarkTheme2) {
                long j = DarkCardBorder;
            } else {
                ColorKt.Color(4278532908L);
            }
            long titleColor = isDarkTheme2 ? Color.Companion.getWhite-0d7_KjU() : ColorKt.Color(4279179050L);
            long arabicColor = isDarkTheme2 ? ShimmerGold : ColorKt.Color(4278532908L);
            long descColor = isDarkTheme2 ? Color.copy-wmQWz5c$default(ParchmentGold, 0.9f, 0.0f, 0.0f, 0.0f, 14, (Object) null) : ColorKt.Color(4282865001L);
            long subRuleColor = isDarkTheme2 ? ShimmerGold : ColorKt.Color(4278556265L);
            long statsTextColor = isDarkTheme2 ? TextMutedGreen : ColorKt.Color(4282865001L);
            Modifier clip = ClipKt.clip(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, (Object) null), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(24)));
            float f = Dp.constructor-impl((float) 1.8d);
            if (isDarkTheme2) {
                Brush.Companion companion = Brush.Companion;
                Color[] colorArr = new Color[3];
                colorArr[0] = Color.box-impl(ShimmerGold);
                colorArr[1] = Color.box-impl(LuxuryGold);
                colorArr[c] = Color.box-impl(BrightGold);
                brush = Brush.Companion.linearGradient-mHitzGk$default(companion, CollectionsKt.listOf(colorArr), 0L, 0L, 0, 14, (Object) null);
            } else {
                Brush.Companion companion2 = Brush.Companion;
                Color[] colorArr2 = new Color[3];
                colorArr2[0] = Color.box-impl(ColorKt.Color(4278532908L));
                colorArr2[1] = Color.box-impl(ColorKt.Color(4279286145L));
                colorArr2[c] = Color.box-impl(ColorKt.Color(4291405900L));
                brush = Brush.Companion.linearGradient-mHitzGk$default(companion2, CollectionsKt.listOf(colorArr2), 0L, 0L, 0, 14, (Object) null);
            }
            Modifier border = BorderKt.border(clip, new BorderStroke(f, brush, (DefaultConstructorMarker) null), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(24)));
            ComposerKt.sourceInformationMarkerStart($composer3, 1565607422, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            boolean z2 = ($dirty & 896) == 256;
            Object rememberedValue2 = $composer3.rememberedValue();
            if (z2 || rememberedValue2 == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda82
                    public final Object invoke() {
                        return LiveDhikrScreenKt.GrandUmrahMegaPoster$lambda$283$lambda$282(function0);
                    }
                };
                $composer3.updateRememberedValue(obj);
            } else {
                obj = rememberedValue2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer3);
            Modifier modifier = ClickableKt.clickable-XHw0xAI$default(border, false, (String) null, (Role) null, (Function0) obj, 7, (Object) null);
            Shape shape = RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(24));
            CardColors cardColors = CardDefaults.INSTANCE.cardColors-ro_MJ88(cardBg, 0L, 0L, 0L, $composer3, CardDefaults.$stable << 12, 14);
            CardElevation cardElevation = CardDefaults.INSTANCE.cardElevation-aqJV_2Y(isDarkTheme2 ? Dp.constructor-impl(0) : Dp.constructor-impl(4), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, $composer3, CardDefaults.$stable << 18, 62);
            final boolean isDarkTheme4 = isDarkTheme2;
            final Function0 onViewParticipants4 = onViewParticipants;
            final long titleColor2 = titleColor;
            final long arabicColor2 = arabicColor;
            final long statsTextColor2 = statsTextColor;
            final long descColor2 = descColor;
            final long subRuleColor2 = subRuleColor;
            $composer2 = $composer3;
            CardKt.Card(modifier, shape, cardColors, cardElevation, (BorderStroke) null, ComposableLambdaKt.rememberComposableLambda(455262047, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda83
                public final Object invoke(Object obj3, Object obj4, Object obj5) {
                    return LiveDhikrScreenKt.GrandUmrahMegaPoster$lambda$307(campaign, isDarkTheme4, function0, arabicColor2, titleColor2, descColor2, subRuleColor2, onViewParticipants4, function02, statsTextColor2, (ColumnScope) obj3, (Composer) obj4, ((Integer) obj5).intValue());
                }
            }, $composer2, 54), $composer2, 196608, 16);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            isDarkTheme3 = isDarkTheme4;
            onViewParticipants2 = onViewParticipants4;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda84
                public final Object invoke(Object obj3, Object obj4) {
                    return LiveDhikrScreenKt.GrandUmrahMegaPoster$lambda$308(campaign, isDarkTheme3, function0, function02, onViewParticipants2, $changed, i, (Composer) obj3, ((Integer) obj4).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GrandUmrahMegaPoster$lambda$283$lambda$282(Function0 $onJoinClick) {
        $onJoinClick.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x08e1  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0907  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0991  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x099d  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x09d6  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0a47  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0b36  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0b42  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0b7b  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0bf0  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0d08  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0e0a  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0e53  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0e78  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0f0b  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0f17  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0f50  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x1029  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x1035  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x106e  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x1153  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x115f  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x1198  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x121e  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x127b  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x12f1  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x1341  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x1351  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x142b  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x1437  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x1470  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x1508  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x15b6  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x15c2  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x15fb  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x16a9  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x1790  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x17ba  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x17ef  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x188c  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x1898  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x18cf  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x1940  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x19d4  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x19f6  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x1a6f  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x1a11  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x19d7  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x1943  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x18e5  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x189e  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x17bd  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x1798  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x16b0  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x1611 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:270:0x15c8  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x1516 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:274:0x1486 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:275:0x143d  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x1359  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x1344  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x12fe A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:280:0x127e  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x1224  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x11ae A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:284:0x1165  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x1084 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:287:0x103b  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x0f66 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01e1  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x0f1d  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x0e80  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x0e5a  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x0e26  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0d39  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x0c97  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x0b91 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:298:0x0b48  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x0a4a  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x09ec A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:302:0x09a3  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x090a  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x08e9  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x084f  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x07de  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x07c5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:322:0x073f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:323:0x06f6  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x0654  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x062e  */
    /* JADX WARN: Removed duplicated region for block: B:326:0x05ac  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x054a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0501  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x040c  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x0356 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:333:0x030d  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x02fb  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0307  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0340  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03ad  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x03d1  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x04ef  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x04fb  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0534  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x05a9  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0608  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0628  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x064e  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x06e4  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x06f0  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0729  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x079a  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x07d7  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x084b  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit GrandUmrahMegaPoster$lambda$307(com.tasbih.digitaltasbih.data.groups.CampaignItem r164, final boolean r165, kotlin.jvm.functions.Function0 r166, long r167, long r169, long r171, long r173, final kotlin.jvm.functions.Function0 r175, final kotlin.jvm.functions.Function0 r176, long r177, androidx.compose.foundation.layout.ColumnScope r179, androidx.compose.runtime.Composer r180, int r181) {
        /*
            Method dump skipped, instructions count: 6773
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.GrandUmrahMegaPoster$lambda$307(com.tasbih.digitaltasbih.data.groups.CampaignItem, boolean, kotlin.jvm.functions.Function0, long, long, long, long, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, long, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final float GrandUmrahMegaPoster$lambda$307$lambda$306$lambda$297$lambda$296$lambda$295$lambda$294(float $pct) {
        return $pct;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GrandUmrahMegaPoster$lambda$307$lambda$306$lambda$304$lambda$299$lambda$298(Function0 $onViewParticipants) {
        $onViewParticipants.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GrandUmrahMegaPoster$lambda$307$lambda$306$lambda$304$lambda$302$lambda$301(Function0 $onRegisterClick) {
        $onRegisterClick.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit GrandUmrahMegaPoster$lambda$307$lambda$306$lambda$305(boolean $isDarkTheme, RowScope $this$Button, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C1559@71443L263:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1732451131, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.GrandUmrahMegaPoster.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:1559)");
            }
            TextKt.Text--4IGK_g("Join Blessed Muhim & Recite Live ➔", (Modifier) null, $isDarkTheme ? ColorKt.Color(4280163086L) : Color.Companion.getWhite-0d7_KjU(), TextUnitKt.getSp(13.5d), (FontStyle) null, FontWeight.Companion.getExtraBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 199686, 0, 131026);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0322  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0339  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x041b  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x032f  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0324  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void CampaignCard(final com.tasbih.digitaltasbih.data.groups.CampaignItem r45, final boolean r46, final boolean r47, boolean r48, boolean r49, androidx.compose.ui.Modifier r50, final kotlin.jvm.functions.Function0<kotlin.Unit> r51, final kotlin.jvm.functions.Function0<kotlin.Unit> r52, final kotlin.jvm.functions.Function0<kotlin.Unit> r53, final kotlin.jvm.functions.Function0<kotlin.Unit> r54, kotlin.jvm.functions.Function0<kotlin.Unit> r55, kotlin.jvm.functions.Function0<kotlin.Unit> r56, androidx.compose.runtime.Composer r57, final int r58, final int r59, final int r60) {
        /*
            Method dump skipped, instructions count: 1103
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.CampaignCard(com.tasbih.digitaltasbih.data.groups.CampaignItem, boolean, boolean, boolean, boolean, androidx.compose.ui.Modifier, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CampaignCard$lambda$315$lambda$314(boolean $isJoined, Function0 $onEnter, Function0 $onJoin) {
        if ($isJoined) {
            $onEnter.invoke();
        } else {
            $onJoin.invoke();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0b82  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0bbb  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0c26  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0c8e  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0d91  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0d9d  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0dd6  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0eb3  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0ebf  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0ef8  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0f65  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0fb7  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x1092  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x109e  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x10d7  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x1142  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x1194  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x1238  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x125c  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x12ec  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x12f8  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x1331  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x140a  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x1416  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x144f  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x14c4  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x1635  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x165c  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x16e5  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x16f1  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x172a  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x180a  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x1816  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x184f  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x1933  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x193f  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x1978  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x1a51  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x1a5d  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x1a96  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x1b16  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x1c06  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x1c12  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x1c4b  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x1cc9  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x1da2  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x1e13  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x1e1f  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x1e58  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x1fcb  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x1fd7  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x2010  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x2083  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01f2  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x2184  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x21aa  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x21e4  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x227c  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x2288  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x22c1  */
    /* JADX WARN: Removed duplicated region for block: B:312:0x23a4  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x23b0  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x23e9  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x24c2  */
    /* JADX WARN: Removed duplicated region for block: B:326:0x24ce  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x2507  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x25dc  */
    /* JADX WARN: Removed duplicated region for block: B:337:0x263a  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x26ad  */
    /* JADX WARN: Removed duplicated region for block: B:360:0x2955  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x2961  */
    /* JADX WARN: Removed duplicated region for block: B:366:0x299a  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x02fe  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x2ad5  */
    /* JADX WARN: Removed duplicated region for block: B:376:0x2b65  */
    /* JADX WARN: Removed duplicated region for block: B:379:0x2b71  */
    /* JADX WARN: Removed duplicated region for block: B:382:0x2baa  */
    /* JADX WARN: Removed duplicated region for block: B:387:0x2c82  */
    /* JADX WARN: Removed duplicated region for block: B:390:0x2c8e  */
    /* JADX WARN: Removed duplicated region for block: B:393:0x2cc7  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x2d2c  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x030a  */
    /* JADX WARN: Removed duplicated region for block: B:401:0x2d7b  */
    /* JADX WARN: Removed duplicated region for block: B:404:0x2e0b  */
    /* JADX WARN: Removed duplicated region for block: B:407:0x2e57  */
    /* JADX WARN: Removed duplicated region for block: B:412:0x2eda  */
    /* JADX WARN: Removed duplicated region for block: B:415:0x2ee6  */
    /* JADX WARN: Removed duplicated region for block: B:418:0x2f1f  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x2f8c  */
    /* JADX WARN: Removed duplicated region for block: B:426:0x3029  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0343  */
    /* JADX WARN: Removed duplicated region for block: B:431:0x30ac  */
    /* JADX WARN: Removed duplicated region for block: B:434:0x30b8  */
    /* JADX WARN: Removed duplicated region for block: B:437:0x30f1  */
    /* JADX WARN: Removed duplicated region for block: B:442:0x3158  */
    /* JADX WARN: Removed duplicated region for block: B:445:0x3169  */
    /* JADX WARN: Removed duplicated region for block: B:448:0x31ad  */
    /* JADX WARN: Removed duplicated region for block: B:473:0x33e1  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03c1  */
    /* JADX WARN: Removed duplicated region for block: B:480:0x33a2  */
    /* JADX WARN: Removed duplicated region for block: B:481:0x316c  */
    /* JADX WARN: Removed duplicated region for block: B:482:0x315f  */
    /* JADX WARN: Removed duplicated region for block: B:484:0x3107  */
    /* JADX WARN: Removed duplicated region for block: B:485:0x30be  */
    /* JADX WARN: Removed duplicated region for block: B:487:0x2f8f  */
    /* JADX WARN: Removed duplicated region for block: B:489:0x2f35  */
    /* JADX WARN: Removed duplicated region for block: B:490:0x2eec  */
    /* JADX WARN: Removed duplicated region for block: B:492:0x2e13  */
    /* JADX WARN: Removed duplicated region for block: B:493:0x2d7e  */
    /* JADX WARN: Removed duplicated region for block: B:494:0x2d33  */
    /* JADX WARN: Removed duplicated region for block: B:496:0x2cdd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:497:0x2c94  */
    /* JADX WARN: Removed duplicated region for block: B:499:0x2bc0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:500:0x2b77  */
    /* JADX WARN: Removed duplicated region for block: B:502:0x2ae3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:504:0x29b0  */
    /* JADX WARN: Removed duplicated region for block: B:505:0x2967  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x03e7  */
    /* JADX WARN: Removed duplicated region for block: B:511:0x28bd  */
    /* JADX WARN: Removed duplicated region for block: B:512:0x263d  */
    /* JADX WARN: Removed duplicated region for block: B:513:0x25df  */
    /* JADX WARN: Removed duplicated region for block: B:515:0x251d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:516:0x24d4  */
    /* JADX WARN: Removed duplicated region for block: B:518:0x23ff A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:519:0x23b6  */
    /* JADX WARN: Removed duplicated region for block: B:521:0x22d7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:522:0x228e  */
    /* JADX WARN: Removed duplicated region for block: B:524:0x21b2  */
    /* JADX WARN: Removed duplicated region for block: B:525:0x218c  */
    /* JADX WARN: Removed duplicated region for block: B:526:0x2086  */
    /* JADX WARN: Removed duplicated region for block: B:528:0x2026  */
    /* JADX WARN: Removed duplicated region for block: B:529:0x1fdd  */
    /* JADX WARN: Removed duplicated region for block: B:531:0x1e6e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:532:0x1e25  */
    /* JADX WARN: Removed duplicated region for block: B:533:0x1da6  */
    /* JADX WARN: Removed duplicated region for block: B:534:0x1ccc  */
    /* JADX WARN: Removed duplicated region for block: B:536:0x1c61 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:537:0x1c18  */
    /* JADX WARN: Removed duplicated region for block: B:538:0x1b19  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x047f  */
    /* JADX WARN: Removed duplicated region for block: B:540:0x1aac A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:541:0x1a63  */
    /* JADX WARN: Removed duplicated region for block: B:543:0x198e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:544:0x1945  */
    /* JADX WARN: Removed duplicated region for block: B:546:0x1865 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:547:0x181c  */
    /* JADX WARN: Removed duplicated region for block: B:549:0x1740 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:550:0x16f7  */
    /* JADX WARN: Removed duplicated region for block: B:551:0x1664  */
    /* JADX WARN: Removed duplicated region for block: B:552:0x163d  */
    /* JADX WARN: Removed duplicated region for block: B:554:0x157c  */
    /* JADX WARN: Removed duplicated region for block: B:556:0x1465 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:557:0x141c  */
    /* JADX WARN: Removed duplicated region for block: B:559:0x1347 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:560:0x12fe  */
    /* JADX WARN: Removed duplicated region for block: B:561:0x1264  */
    /* JADX WARN: Removed duplicated region for block: B:562:0x1240  */
    /* JADX WARN: Removed duplicated region for block: B:563:0x1145  */
    /* JADX WARN: Removed duplicated region for block: B:565:0x10ed A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:566:0x10a4  */
    /* JADX WARN: Removed duplicated region for block: B:567:0x0f68  */
    /* JADX WARN: Removed duplicated region for block: B:569:0x0f0e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x048b  */
    /* JADX WARN: Removed duplicated region for block: B:570:0x0ec5  */
    /* JADX WARN: Removed duplicated region for block: B:572:0x0dec A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:573:0x0da3  */
    /* JADX WARN: Removed duplicated region for block: B:574:0x0ca2  */
    /* JADX WARN: Removed duplicated region for block: B:575:0x0c3a  */
    /* JADX WARN: Removed duplicated region for block: B:577:0x0bd1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:578:0x0b88  */
    /* JADX WARN: Removed duplicated region for block: B:585:0x0904  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x04c4  */
    /* JADX WARN: Removed duplicated region for block: B:605:0x0aeb  */
    /* JADX WARN: Removed duplicated region for block: B:606:0x066c  */
    /* JADX WARN: Removed duplicated region for block: B:608:0x05f2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:609:0x05a9  */
    /* JADX WARN: Removed duplicated region for block: B:611:0x04da A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:612:0x0491  */
    /* JADX WARN: Removed duplicated region for block: B:613:0x03ef  */
    /* JADX WARN: Removed duplicated region for block: B:614:0x03c9  */
    /* JADX WARN: Removed duplicated region for block: B:616:0x0359 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:617:0x0310  */
    /* JADX WARN: Removed duplicated region for block: B:620:0x01f8  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0597  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x05a3  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x05dc  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0669  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x06e0  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0b76  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CampaignCard$lambda$358(boolean r181, final kotlin.jvm.functions.Function0 r182, com.tasbih.digitaltasbih.data.groups.CampaignItem r183, boolean r184, java.lang.String r185, long r186, long r188, long r190, long r192, int r194, final boolean r195, final kotlin.jvm.functions.Function0 r196, final kotlin.jvm.functions.Function0 r197, final kotlin.jvm.functions.Function0 r198, final kotlin.jvm.functions.Function0 r199, final kotlin.jvm.functions.Function0 r200, boolean r201, androidx.compose.foundation.layout.ColumnScope r202, androidx.compose.runtime.Composer r203, int r204) {
        /*
            Method dump skipped, instructions count: 13287
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.CampaignCard$lambda$358(boolean, kotlin.jvm.functions.Function0, com.tasbih.digitaltasbih.data.groups.CampaignItem, boolean, java.lang.String, long, long, long, long, int, boolean, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, boolean, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CampaignCard$lambda$358$lambda$357$lambda$338$lambda$337(Function0 $onViewLeaderboard) {
        $onViewLeaderboard.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CampaignCard$lambda$358$lambda$357$lambda$356$lambda$344$lambda$343(boolean $isJoined, Function0 $onEnter, Function0 $onJoin) {
        if ($isJoined) {
            $onEnter.invoke();
        } else {
            $onJoin.invoke();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CampaignCard$lambda$358$lambda$357$lambda$356$lambda$348$lambda$347(Function0 $onShare) {
        $onShare.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CampaignCard$lambda$358$lambda$357$lambda$356$lambda$351$lambda$350(Function0 $onToggleSave) {
        $onToggleSave.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CampaignCard$lambda$358$lambda$357$lambda$356$lambda$354$lambda$353(Function0 $onDelete) {
        $onDelete.invoke();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code restructure failed: missing block: B:973:0x04dc, code lost:
    
        if (r12 > 0.0f) goto L191;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:102:0x03c1  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x03f9  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x051d  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0558  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0588  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x05ba  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x05f0  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0626  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x065e  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x06a2  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x06cf  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x06fc  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x072b  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x076b  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x07a8  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x07d5  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0815  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0850  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x088e  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x08ce  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x090b  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0936  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0941  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x094c  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0953  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x097c  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x09b8  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x09ee  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0a20  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0a54  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0ac3  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0b5f  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0b6f  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0b81  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0c65  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0c7c  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x0ce1  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0cf3  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x0d3a  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0d53  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0d66  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0d73  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0d9b  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0dd9  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x0e63  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0e6f  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0ea2  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x0fa7  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x0fb3  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0fe6  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x10fc  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x1108  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x1141  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x1222  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x122e  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x1267  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x12e3  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x12f6  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x1308  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x1527  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x1533  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x156c  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x1645  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x1651  */
    /* JADX WARN: Removed duplicated region for block: B:322:0x168a  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x17ca  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x17e6  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x183a  */
    /* JADX WARN: Removed duplicated region for block: B:338:0x1898  */
    /* JADX WARN: Removed duplicated region for block: B:341:0x192a  */
    /* JADX WARN: Removed duplicated region for block: B:345:0x193c  */
    /* JADX WARN: Removed duplicated region for block: B:351:0x1a0e  */
    /* JADX WARN: Removed duplicated region for block: B:354:0x1b2b  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x1baf  */
    /* JADX WARN: Removed duplicated region for block: B:360:0x1bbb  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x1bf4  */
    /* JADX WARN: Removed duplicated region for block: B:368:0x1c92  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x1d22  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x1d2e  */
    /* JADX WARN: Removed duplicated region for block: B:377:0x1d67  */
    /* JADX WARN: Removed duplicated region for block: B:382:0x1df1  */
    /* JADX WARN: Removed duplicated region for block: B:385:0x1e3f  */
    /* JADX WARN: Removed duplicated region for block: B:388:0x1ecc  */
    /* JADX WARN: Removed duplicated region for block: B:391:0x1ed8  */
    /* JADX WARN: Removed duplicated region for block: B:394:0x1f11  */
    /* JADX WARN: Removed duplicated region for block: B:399:0x1f7c  */
    /* JADX WARN: Removed duplicated region for block: B:405:0x204d  */
    /* JADX WARN: Removed duplicated region for block: B:408:0x20a0  */
    /* JADX WARN: Removed duplicated region for block: B:411:0x213c  */
    /* JADX WARN: Removed duplicated region for block: B:414:0x2148  */
    /* JADX WARN: Removed duplicated region for block: B:417:0x2181  */
    /* JADX WARN: Removed duplicated region for block: B:422:0x2259  */
    /* JADX WARN: Removed duplicated region for block: B:425:0x2265  */
    /* JADX WARN: Removed duplicated region for block: B:428:0x229e  */
    /* JADX WARN: Removed duplicated region for block: B:433:0x2353  */
    /* JADX WARN: Removed duplicated region for block: B:436:0x23e2  */
    /* JADX WARN: Removed duplicated region for block: B:453:0x2534  */
    /* JADX WARN: Removed duplicated region for block: B:462:0x2bc2  */
    /* JADX WARN: Removed duplicated region for block: B:465:0x2bf2  */
    /* JADX WARN: Removed duplicated region for block: B:468:0x2c13  */
    /* JADX WARN: Removed duplicated region for block: B:471:0x2c4c  */
    /* JADX WARN: Removed duplicated region for block: B:474:0x2ce6  */
    /* JADX WARN: Removed duplicated region for block: B:477:0x2cf2  */
    /* JADX WARN: Removed duplicated region for block: B:480:0x2d2b  */
    /* JADX WARN: Removed duplicated region for block: B:485:0x2e14  */
    /* JADX WARN: Removed duplicated region for block: B:488:0x2e20  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x4440  */
    /* JADX WARN: Removed duplicated region for block: B:491:0x2e59  */
    /* JADX WARN: Removed duplicated region for block: B:496:0x2f33  */
    /* JADX WARN: Removed duplicated region for block: B:499:0x2f3f  */
    /* JADX WARN: Removed duplicated region for block: B:502:0x2f78  */
    /* JADX WARN: Removed duplicated region for block: B:507:0x309b  */
    /* JADX WARN: Removed duplicated region for block: B:510:0x30a7  */
    /* JADX WARN: Removed duplicated region for block: B:513:0x30e0  */
    /* JADX WARN: Removed duplicated region for block: B:518:0x3157  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x445c  */
    /* JADX WARN: Removed duplicated region for block: B:521:0x31ab  */
    /* JADX WARN: Removed duplicated region for block: B:524:0x328e  */
    /* JADX WARN: Removed duplicated region for block: B:527:0x329a  */
    /* JADX WARN: Removed duplicated region for block: B:530:0x32d3  */
    /* JADX WARN: Removed duplicated region for block: B:535:0x3348  */
    /* JADX WARN: Removed duplicated region for block: B:538:0x33a1  */
    /* JADX WARN: Removed duplicated region for block: B:541:0x345a  */
    /* JADX WARN: Removed duplicated region for block: B:544:0x34d2  */
    /* JADX WARN: Removed duplicated region for block: B:547:0x34de  */
    /* JADX WARN: Removed duplicated region for block: B:550:0x3512  */
    /* JADX WARN: Removed duplicated region for block: B:555:0x3624  */
    /* JADX WARN: Removed duplicated region for block: B:558:0x363b  */
    /* JADX WARN: Removed duplicated region for block: B:563:0x3729  */
    /* JADX WARN: Removed duplicated region for block: B:566:0x3735  */
    /* JADX WARN: Removed duplicated region for block: B:569:0x376e  */
    /* JADX WARN: Removed duplicated region for block: B:574:0x3853  */
    /* JADX WARN: Removed duplicated region for block: B:577:0x385f  */
    /* JADX WARN: Removed duplicated region for block: B:580:0x3898  */
    /* JADX WARN: Removed duplicated region for block: B:585:0x3976  */
    /* JADX WARN: Removed duplicated region for block: B:588:0x3982  */
    /* JADX WARN: Removed duplicated region for block: B:591:0x39bb  */
    /* JADX WARN: Removed duplicated region for block: B:596:0x3b1c  */
    /* JADX WARN: Removed duplicated region for block: B:599:0x3b28  */
    /* JADX WARN: Removed duplicated region for block: B:602:0x3b5f  */
    /* JADX WARN: Removed duplicated region for block: B:607:0x3de0  */
    /* JADX WARN: Removed duplicated region for block: B:619:0x3ecd  */
    /* JADX WARN: Removed duplicated region for block: B:628:0x3f7f  */
    /* JADX WARN: Removed duplicated region for block: B:644:0x4027  */
    /* JADX WARN: Removed duplicated region for block: B:651:0x409d  */
    /* JADX WARN: Removed duplicated region for block: B:655:0x40d4  */
    /* JADX WARN: Removed duplicated region for block: B:661:0x4140  */
    /* JADX WARN: Removed duplicated region for block: B:667:0x41a0  */
    /* JADX WARN: Removed duplicated region for block: B:684:0x428f  */
    /* JADX WARN: Removed duplicated region for block: B:699:0x43cf  */
    /* JADX WARN: Removed duplicated region for block: B:705:0x4431  */
    /* JADX WARN: Removed duplicated region for block: B:708:0x43dd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:718:0x4403  */
    /* JADX WARN: Removed duplicated region for block: B:722:0x4272  */
    /* JADX WARN: Removed duplicated region for block: B:724:0x418d  */
    /* JADX WARN: Removed duplicated region for block: B:726:0x412c  */
    /* JADX WARN: Removed duplicated region for block: B:727:0x40ac  */
    /* JADX WARN: Removed duplicated region for block: B:730:0x40ba  */
    /* JADX WARN: Removed duplicated region for block: B:734:0x400a  */
    /* JADX WARN: Removed duplicated region for block: B:737:0x3f64  */
    /* JADX WARN: Removed duplicated region for block: B:741:0x3eb0  */
    /* JADX WARN: Removed duplicated region for block: B:743:0x3b75 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:744:0x3b2e  */
    /* JADX WARN: Removed duplicated region for block: B:746:0x39d1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:747:0x3988  */
    /* JADX WARN: Removed duplicated region for block: B:749:0x38ae A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:750:0x3865  */
    /* JADX WARN: Removed duplicated region for block: B:752:0x3784 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:753:0x373b  */
    /* JADX WARN: Removed duplicated region for block: B:755:0x3627  */
    /* JADX WARN: Removed duplicated region for block: B:757:0x3528  */
    /* JADX WARN: Removed duplicated region for block: B:758:0x34e2  */
    /* JADX WARN: Removed duplicated region for block: B:759:0x346a  */
    /* JADX WARN: Removed duplicated region for block: B:760:0x33a4  */
    /* JADX WARN: Removed duplicated region for block: B:761:0x334b  */
    /* JADX WARN: Removed duplicated region for block: B:763:0x32e9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:764:0x32a0  */
    /* JADX WARN: Removed duplicated region for block: B:765:0x31ae  */
    /* JADX WARN: Removed duplicated region for block: B:766:0x315a  */
    /* JADX WARN: Removed duplicated region for block: B:768:0x30f6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:769:0x30ad  */
    /* JADX WARN: Removed duplicated region for block: B:771:0x2f8e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:772:0x2f45  */
    /* JADX WARN: Removed duplicated region for block: B:774:0x2e6f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:775:0x2e26  */
    /* JADX WARN: Removed duplicated region for block: B:777:0x2d41 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:778:0x2cf8  */
    /* JADX WARN: Removed duplicated region for block: B:779:0x2c5a  */
    /* JADX WARN: Removed duplicated region for block: B:780:0x2c1b  */
    /* JADX WARN: Removed duplicated region for block: B:781:0x2bfa  */
    /* JADX WARN: Removed duplicated region for block: B:782:0x2bd0  */
    /* JADX WARN: Removed duplicated region for block: B:785:0x253a  */
    /* JADX WARN: Removed duplicated region for block: B:790:0x2606  */
    /* JADX WARN: Removed duplicated region for block: B:803:0x27e0  */
    /* JADX WARN: Removed duplicated region for block: B:806:0x283e  */
    /* JADX WARN: Removed duplicated region for block: B:809:0x28fa  */
    /* JADX WARN: Removed duplicated region for block: B:812:0x2906  */
    /* JADX WARN: Removed duplicated region for block: B:820:0x29ce  */
    /* JADX WARN: Removed duplicated region for block: B:823:0x2a4a  */
    /* JADX WARN: Removed duplicated region for block: B:827:0x2a57  */
    /* JADX WARN: Removed duplicated region for block: B:830:0x2a78  */
    /* JADX WARN: Removed duplicated region for block: B:833:0x2aac  */
    /* JADX WARN: Removed duplicated region for block: B:836:0x2aec  */
    /* JADX WARN: Removed duplicated region for block: B:841:0x2b06 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:842:0x2aba  */
    /* JADX WARN: Removed duplicated region for block: B:843:0x2a88  */
    /* JADX WARN: Removed duplicated region for block: B:844:0x2a5d  */
    /* JADX WARN: Removed duplicated region for block: B:846:0x29d5  */
    /* JADX WARN: Removed duplicated region for block: B:849:0x290c  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0274  */
    /* JADX WARN: Removed duplicated region for block: B:850:0x2850  */
    /* JADX WARN: Removed duplicated region for block: B:851:0x27e6  */
    /* JADX WARN: Removed duplicated region for block: B:855:0x2356  */
    /* JADX WARN: Removed duplicated region for block: B:857:0x22b4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:858:0x226b  */
    /* JADX WARN: Removed duplicated region for block: B:860:0x2197 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:861:0x214e  */
    /* JADX WARN: Removed duplicated region for block: B:862:0x20b1  */
    /* JADX WARN: Removed duplicated region for block: B:863:0x2053  */
    /* JADX WARN: Removed duplicated region for block: B:865:0x1fd6  */
    /* JADX WARN: Removed duplicated region for block: B:867:0x1f27 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:868:0x1ede  */
    /* JADX WARN: Removed duplicated region for block: B:869:0x1e51  */
    /* JADX WARN: Removed duplicated region for block: B:870:0x1df7  */
    /* JADX WARN: Removed duplicated region for block: B:872:0x1d7d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:873:0x1d34  */
    /* JADX WARN: Removed duplicated region for block: B:874:0x1ca4  */
    /* JADX WARN: Removed duplicated region for block: B:876:0x1c0a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:877:0x1bc1  */
    /* JADX WARN: Removed duplicated region for block: B:878:0x1b3e  */
    /* JADX WARN: Removed duplicated region for block: B:879:0x1a1c  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x02b6  */
    /* JADX WARN: Removed duplicated region for block: B:881:0x1990  */
    /* JADX WARN: Removed duplicated region for block: B:883:0x18ae  */
    /* JADX WARN: Removed duplicated region for block: B:884:0x184c  */
    /* JADX WARN: Removed duplicated region for block: B:886:0x17cd  */
    /* JADX WARN: Removed duplicated region for block: B:888:0x16a0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:889:0x1657  */
    /* JADX WARN: Removed duplicated region for block: B:891:0x1582 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:892:0x1539  */
    /* JADX WARN: Removed duplicated region for block: B:894:0x12f9  */
    /* JADX WARN: Removed duplicated region for block: B:895:0x12e6  */
    /* JADX WARN: Removed duplicated region for block: B:897:0x127d  */
    /* JADX WARN: Removed duplicated region for block: B:898:0x1234  */
    /* JADX WARN: Removed duplicated region for block: B:900:0x1157  */
    /* JADX WARN: Removed duplicated region for block: B:901:0x110e  */
    /* JADX WARN: Removed duplicated region for block: B:903:0x0ffc  */
    /* JADX WARN: Removed duplicated region for block: B:904:0x0fb7  */
    /* JADX WARN: Removed duplicated region for block: B:906:0x0eb8  */
    /* JADX WARN: Removed duplicated region for block: B:907:0x0e73  */
    /* JADX WARN: Removed duplicated region for block: B:908:0x0dec  */
    /* JADX WARN: Removed duplicated region for block: B:909:0x0db3  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x02ed  */
    /* JADX WARN: Removed duplicated region for block: B:910:0x0d76  */
    /* JADX WARN: Removed duplicated region for block: B:911:0x0d69  */
    /* JADX WARN: Removed duplicated region for block: B:912:0x0d59  */
    /* JADX WARN: Removed duplicated region for block: B:913:0x0d47  */
    /* JADX WARN: Removed duplicated region for block: B:915:0x0d03 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:916:0x0ce4  */
    /* JADX WARN: Removed duplicated region for block: B:918:0x0c67  */
    /* JADX WARN: Removed duplicated region for block: B:920:0x0b72  */
    /* JADX WARN: Removed duplicated region for block: B:921:0x0b62  */
    /* JADX WARN: Removed duplicated region for block: B:923:0x0ae3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:924:0x0a6e  */
    /* JADX WARN: Removed duplicated region for block: B:925:0x0a2f  */
    /* JADX WARN: Removed duplicated region for block: B:926:0x09fb  */
    /* JADX WARN: Removed duplicated region for block: B:927:0x09c7  */
    /* JADX WARN: Removed duplicated region for block: B:928:0x098d  */
    /* JADX WARN: Removed duplicated region for block: B:929:0x0956  */
    /* JADX WARN: Removed duplicated region for block: B:930:0x094f  */
    /* JADX WARN: Removed duplicated region for block: B:931:0x0944  */
    /* JADX WARN: Removed duplicated region for block: B:932:0x0939  */
    /* JADX WARN: Removed duplicated region for block: B:933:0x0924  */
    /* JADX WARN: Removed duplicated region for block: B:934:0x08e7  */
    /* JADX WARN: Removed duplicated region for block: B:935:0x08a7  */
    /* JADX WARN: Removed duplicated region for block: B:936:0x0867  */
    /* JADX WARN: Removed duplicated region for block: B:937:0x082c  */
    /* JADX WARN: Removed duplicated region for block: B:938:0x07ee  */
    /* JADX WARN: Removed duplicated region for block: B:939:0x07b5  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0321  */
    /* JADX WARN: Removed duplicated region for block: B:940:0x0784  */
    /* JADX WARN: Removed duplicated region for block: B:941:0x0744  */
    /* JADX WARN: Removed duplicated region for block: B:942:0x070b  */
    /* JADX WARN: Removed duplicated region for block: B:943:0x06dc  */
    /* JADX WARN: Removed duplicated region for block: B:944:0x06af  */
    /* JADX WARN: Removed duplicated region for block: B:946:0x066d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:947:0x063b  */
    /* JADX WARN: Removed duplicated region for block: B:948:0x0605  */
    /* JADX WARN: Removed duplicated region for block: B:949:0x05cf  */
    /* JADX WARN: Removed duplicated region for block: B:950:0x059b  */
    /* JADX WARN: Removed duplicated region for block: B:951:0x0569  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0355  */
    /* JADX WARN: Removed duplicated region for block: B:988:0x040b  */
    /* JADX WARN: Removed duplicated region for block: B:989:0x03d3  */
    /* JADX WARN: Removed duplicated region for block: B:990:0x039b  */
    /* JADX WARN: Removed duplicated region for block: B:991:0x0365  */
    /* JADX WARN: Removed duplicated region for block: B:992:0x0331  */
    /* JADX WARN: Removed duplicated region for block: B:993:0x02fd  */
    /* JADX WARN: Removed duplicated region for block: B:994:0x02c1  */
    /* JADX WARN: Removed duplicated region for block: B:995:0x0291  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0389  */
    /* JADX WARN: Type inference failed for: r18v3, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r18v30 */
    /* JADX WARN: Type inference failed for: r18v31 */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void LiveActiveDhikrRoom(@org.jetbrains.annotations.NotNull final com.tasbih.digitaltasbih.data.groups.CampaignItem r261, @org.jetbrains.annotations.NotNull final com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository r262, @org.jetbrains.annotations.Nullable java.lang.String r263, @org.jetbrains.annotations.NotNull final kotlin.jvm.functions.Function0<kotlin.Unit> r264, @org.jetbrains.annotations.NotNull final kotlin.jvm.functions.Function0<kotlin.Unit> r265, @org.jetbrains.annotations.NotNull final kotlin.jvm.functions.Function0<kotlin.Unit> r266, boolean r267, @org.jetbrains.annotations.NotNull final kotlin.jvm.functions.Function0<kotlin.Unit> r268, boolean r269, @org.jetbrains.annotations.Nullable kotlin.jvm.functions.Function0<kotlin.Unit> r270, @org.jetbrains.annotations.Nullable androidx.compose.runtime.Composer r271, final int r272, final int r273) {
        /*
            Method dump skipped, instructions count: 17534
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LiveActiveDhikrRoom(com.tasbih.digitaltasbih.data.groups.CampaignItem, com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository, java.lang.String, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, boolean, kotlin.jvm.functions.Function0, boolean, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean LiveActiveDhikrRoom$lambda$364(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$365(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int LiveActiveDhikrRoom$lambda$367(MutableIntState $roomTourStep$delegate) {
        return ((IntState) $roomTourStep$delegate).getIntValue();
    }

    private static final LayoutCoordinates LiveActiveDhikrRoom$lambda$370(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates LiveActiveDhikrRoom$lambda$373(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates LiveActiveDhikrRoom$lambda$376(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates LiveActiveDhikrRoom$lambda$379(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates LiveActiveDhikrRoom$lambda$382(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates LiveActiveDhikrRoom$lambda$385(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final boolean LiveActiveDhikrRoom$lambda$390(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$391(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean LiveActiveDhikrRoom$lambda$393(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$394(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean LiveActiveDhikrRoom$lambda$396(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$397(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean LiveActiveDhikrRoom$lambda$399(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$400(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean LiveActiveDhikrRoom$lambda$402(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$403(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int LiveActiveDhikrRoom$lambda$406(MutableIntState $pendingCount$delegate) {
        return ((IntState) $pendingCount$delegate).getIntValue();
    }

    private static final int LiveActiveDhikrRoom$lambda$409(MutableIntState $sessionCount$delegate) {
        return ((IntState) $sessionCount$delegate).getIntValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final long LiveActiveDhikrRoom$lambda$412(MutableLongState $liveTotal$delegate) {
        return ((LongState) $liveTotal$delegate).getLongValue();
    }

    private static final boolean LiveActiveDhikrRoom$lambda$415(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void LiveActiveDhikrRoom$lambda$416(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean LiveActiveDhikrRoom$lambda$418(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$419(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final long LiveActiveDhikrRoom$lambda$421(MutableLongState $autoSpeedMs$delegate) {
        return ((LongState) $autoSpeedMs$delegate).getLongValue();
    }

    private static final boolean LiveActiveDhikrRoom$lambda$424(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$425(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean LiveActiveDhikrRoom$lambda$427(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$428(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean LiveActiveDhikrRoom$lambda$430(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final boolean LiveActiveDhikrRoom$lambda$433(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$434(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final BeadMarbleSkin LiveActiveDhikrRoom$lambda$436(MutableState<BeadMarbleSkin> mutableState) {
        return (BeadMarbleSkin) ((State) mutableState).getValue();
    }

    private static final boolean LiveActiveDhikrRoom$lambda$439(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$440(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final long LiveActiveDhikrRoom$lambda$442(MutableLongState $lastTapTimestamp$delegate) {
        return ((LongState) $lastTapTimestamp$delegate).getLongValue();
    }

    private static final int LiveActiveDhikrRoom$lambda$446(MutableIntState $hourlyCount$delegate) {
        return ((IntState) $hourlyCount$delegate).getIntValue();
    }

    private static final long LiveActiveDhikrRoom$lambda$449(MutableLongState $hourBucketStart$delegate) {
        return ((LongState) $hourBucketStart$delegate).getLongValue();
    }

    private static final boolean LiveActiveDhikrRoom$lambda$452(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void LiveActiveDhikrRoom$lambda$453(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean LiveActiveDhikrRoom$isRateLimited(boolean isSpecialCampaign, int maxPerHour, MutableLongState hourBucketStart$delegate, MutableIntState hourlyCount$delegate) {
        if (!isSpecialCampaign) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (now - LiveActiveDhikrRoom$lambda$449(hourBucketStart$delegate) > 3600000) {
            hourlyCount$delegate.setIntValue(0);
            hourBucketStart$delegate.setLongValue(now);
        }
        return LiveActiveDhikrRoom$lambda$446(hourlyCount$delegate) >= maxPerHour;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void LiveActiveDhikrRoom$doCountStep(boolean isSpecialCampaign, long minTapIntervalMs, HapticFeedback haptic, MutableLongState lastTapTimestamp$delegate, int maxPerHour, MutableLongState hourBucketStart$delegate, MutableIntState hourlyCount$delegate, MutableState<Boolean> mutableState, MutableIntState pendingCount$delegate, MutableIntState sessionCount$delegate, MutableLongState liveTotal$delegate, MutableState<Boolean> mutableState2, MutableState<Boolean> mutableState3, MutableState<Boolean> mutableState4, int amount) {
        long now = System.currentTimeMillis();
        if (isSpecialCampaign) {
            long elapsed = now - LiveActiveDhikrRoom$lambda$442(lastTapTimestamp$delegate);
            if (LiveActiveDhikrRoom$lambda$442(lastTapTimestamp$delegate) <= 0 || elapsed >= minTapIntervalMs) {
                if (LiveActiveDhikrRoom$isRateLimited(isSpecialCampaign, maxPerHour, hourBucketStart$delegate, hourlyCount$delegate)) {
                    if (!LiveActiveDhikrRoom$lambda$452(mutableState)) {
                        LiveActiveDhikrRoom$lambda$453(mutableState, true);
                        AppToast.INSTANCE.show("Daily limit reached. JazakAllah! Come back tomorrow. 🤲");
                        return;
                    }
                    return;
                }
                hourlyCount$delegate.setIntValue(LiveActiveDhikrRoom$lambda$446(hourlyCount$delegate) + amount);
            } else {
                return;
            }
        }
        lastTapTimestamp$delegate.setLongValue(now);
        pendingCount$delegate.setIntValue(LiveActiveDhikrRoom$lambda$406(pendingCount$delegate) + amount);
        sessionCount$delegate.setIntValue(LiveActiveDhikrRoom$lambda$409(sessionCount$delegate) + amount);
        liveTotal$delegate.setLongValue(LiveActiveDhikrRoom$lambda$412(liveTotal$delegate) + amount);
        boolean isMilestone = LiveActiveDhikrRoom$lambda$409(sessionCount$delegate) % 33 == 0;
        if (LiveActiveDhikrRoom$lambda$430(mutableState2)) {
            if (isMilestone) {
                haptic.performHapticFeedback-CdsT49E(HapticFeedbackType.Companion.getLongPress-5zf0vsI());
            } else {
                haptic.performHapticFeedback-CdsT49E(HapticFeedbackType.Companion.getTextHandleMove-5zf0vsI());
            }
        }
        if (LiveActiveDhikrRoom$lambda$427(mutableState3)) {
            TasbihAudioEngine.playSandalwoodClick$default(TasbihAudioEngine.INSTANCE, 0.0f, 1, (Object) null);
            if (isMilestone) {
                TasbihAudioEngine.INSTANCE.playCompletionChime();
            }
        }
        if (LiveActiveDhikrRoom$lambda$418(mutableState4) && LiveActiveDhikrRoom$lambda$409(sessionCount$delegate) % 100 == 0) {
            LiveActiveDhikrRoom$lambda$419(mutableState4, false);
            if (LiveActiveDhikrRoom$lambda$430(mutableState2)) {
                haptic.performHapticFeedback-CdsT49E(HapticFeedbackType.Companion.getLongPress-5zf0vsI());
            }
            AppToast.INSTANCE.show("Completed 100 recitations. Paused for reflection.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void LiveActiveDhikrRoom$submitBatch(int maxBatchSize, long sessionStartMs, CoroutineScope coroutineScope, MutableIntState pendingCount$delegate, MutableState<Boolean> mutableState, GroupCampaignRepository $repository, CampaignItem $campaign, boolean isSpecialCampaign, MutableLongState liveTotal$delegate) {
        if (LiveActiveDhikrRoom$lambda$406(pendingCount$delegate) > 0 && !LiveActiveDhikrRoom$lambda$415(mutableState)) {
            int countToSync = RangesKt.coerceAtMost(LiveActiveDhikrRoom$lambda$406(pendingCount$delegate), maxBatchSize);
            pendingCount$delegate.setIntValue(RangesKt.coerceAtLeast(LiveActiveDhikrRoom$lambda$406(pendingCount$delegate) - countToSync, 0));
            LiveActiveDhikrRoom$lambda$416(mutableState, true);
            long sessionDurationMs = System.currentTimeMillis() - sessionStartMs;
            BuildersKt.launch$default(coroutineScope, (CoroutineContext) null, (CoroutineStart) null, new LiveActiveDhikrRoom.submitBatch.1($repository, $campaign, countToSync, sessionDurationMs, isSpecialCampaign, liveTotal$delegate, mutableState, (Continuation) null), 3, (Object) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$456$lambda$455(Function0 $onBack, int $maxBatchSize, long $sessionStartMs, CoroutineScope $coroutineScope, MutableIntState $pendingCount$delegate, MutableState $isSubmitting$delegate, GroupCampaignRepository $repository, CampaignItem $campaign, boolean $isSpecialCampaign, MutableLongState $liveTotal$delegate) {
        LiveActiveDhikrRoom$submitBatch($maxBatchSize, $sessionStartMs, $coroutineScope, $pendingCount$delegate, $isSubmitting$delegate, $repository, $campaign, $isSpecialCampaign, $liveTotal$delegate);
        $onBack.invoke();
        return Unit.INSTANCE;
    }

    private static final void LiveActiveDhikrRoom$shareCampaign(CampaignItem $campaign, GroupCampaignRepository $repository, Context context, String userLang) {
        boolean isCreator = $campaign.isCreator() || $repository.isCampaignCreator($campaign.getCampaignId());
        GroupInviteHelper.INSTANCE.shareCampaign(context, $campaign, isCreator, userLang);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$461$lambda$460(MutableState $roomRootCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $roomRootCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$479$lambda$478$lambda$463$lambda$462(Function0 $onBack, int $maxBatchSize, long $sessionStartMs, CoroutineScope $coroutineScope, MutableIntState $pendingCount$delegate, MutableState $isSubmitting$delegate, GroupCampaignRepository $repository, CampaignItem $campaign, boolean $isSpecialCampaign, MutableLongState $liveTotal$delegate) {
        LiveActiveDhikrRoom$submitBatch($maxBatchSize, $sessionStartMs, $coroutineScope, $pendingCount$delegate, $isSubmitting$delegate, $repository, $campaign, $isSpecialCampaign, $liveTotal$delegate);
        $onBack.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$479$lambda$478$lambda$467$lambda$466(CampaignItem $campaign, GroupCampaignRepository $repository, Context $context, String $userLang) {
        LiveActiveDhikrRoom$shareCampaign($campaign, $repository, $context, $userLang);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$479$lambda$478$lambda$469$lambda$468(MutableState $isSoundOn$delegate) {
        LiveActiveDhikrRoom$lambda$428($isSoundOn$delegate, !LiveActiveDhikrRoom$lambda$427($isSoundOn$delegate));
        AppToast.INSTANCE.show(LiveActiveDhikrRoom$lambda$427($isSoundOn$delegate) ? "Sound ON" : "Sound OFF");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x019f  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x014e  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$479$lambda$478$lambda$471(androidx.compose.runtime.MutableState r49, androidx.compose.runtime.Composer r50, int r51) {
        /*
            Method dump skipped, instructions count: 421
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$479$lambda$478$lambda$471(androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$479$lambda$478$lambda$473$lambda$472(MutableIntState $roomTourStep$delegate, MutableState $isRoomTourOpen$delegate) {
        $roomTourStep$delegate.setIntValue(1);
        LiveActiveDhikrRoom$lambda$365($isRoomTourOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0193  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x014e  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$479$lambda$478$lambda$475(boolean r32, androidx.compose.runtime.Composer r33, int r34) {
        /*
            Method dump skipped, instructions count: 409
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$479$lambda$478$lambda$475(boolean, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$479$lambda$478$lambda$477$lambda$476(MutableState $showDeleteConfirmModal$delegate) {
        LiveActiveDhikrRoom$lambda$403($showDeleteConfirmModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$481$lambda$480(MutableState $infoCardCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $infoCardCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:103:0x09df  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0a36  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0ada  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0ae6  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0b1f  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0cf5  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0d01  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0d3a  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0db9  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0e70  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0e7c  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0eb5  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0f8d  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0f99  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0fd2  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x1153  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x115f  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x1198  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x12f9  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x1305  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x133c  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x13cb  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x143d  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x146a  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x150d  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x147c  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x1440  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x13d9  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x1352  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x130b  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x11ae A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:196:0x1165  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x0fe8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0f9f  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x0ecb A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0e82  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x0dc1  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0d50  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x0d07  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0b35 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0aec  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0a44  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x09e7  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0907 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:214:0x08be  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x07fe  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x078c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0743  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x05e6  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x05d7  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x055f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x0516  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0439 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x03f0  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x034e  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x02fd  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x02d1  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x022b  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0173  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01cd  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0224  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x02f7  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x033c  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x03de  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x03ea  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0423  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0504  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0510  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0549  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x05b2  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x05e0  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0731  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x073d  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0776  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x07f6  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x08ac  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x08b8  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x08f1  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$501(com.tasbih.digitaltasbih.data.groups.CampaignItem r162, boolean r163, com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository r164, final androidx.compose.runtime.MutableState r165, long r166, final androidx.compose.runtime.MutableState r168, androidx.compose.runtime.MutableLongState r169, int r170, final androidx.compose.runtime.MutableState r171, androidx.compose.foundation.layout.ColumnScope r172, androidx.compose.runtime.Composer r173, int r174) {
        /*
            Method dump skipped, instructions count: 5395
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$501(com.tasbih.digitaltasbih.data.groups.CampaignItem, boolean, com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository, androidx.compose.runtime.MutableState, long, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableLongState, int, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$501$lambda$500$lambda$483$lambda$482(MutableState $showShareModal$delegate) {
        LiveActiveDhikrRoom$lambda$391($showShareModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$501$lambda$500$lambda$490$lambda$488$lambda$487(MutableState $showAdPrompt$delegate) {
        LiveActiveDhikrRoom$lambda$397($showAdPrompt$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$501$lambda$500$lambda$499$lambda$498$lambda$495$lambda$494(MutableState $showAdPrompt$delegate) {
        LiveActiveDhikrRoom$lambda$397($showAdPrompt$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$501$lambda$500$lambda$499$lambda$498$lambda$497$lambda$496(MutableState $showFazilatModal$delegate) {
        LiveActiveDhikrRoom$lambda$400($showFazilatModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$503$lambda$502(MutableState $dialCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $dialCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$529$lambda$505$lambda$504(MutableState $autoControlCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $autoControlCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$529$lambda$515$lambda$507$lambda$506(MutableState $isBeadsViewMode$delegate) {
        LiveActiveDhikrRoom$lambda$434($isBeadsViewMode$delegate, !LiveActiveDhikrRoom$lambda$433($isBeadsViewMode$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$529$lambda$515$lambda$510$lambda$509$lambda$508(DrawScope $this$Canvas) {
        Intrinsics.checkNotNullParameter($this$Canvas, "$this$Canvas");
        float r = Size.getMinDimension-impl($this$Canvas.getSize-NH-jbRc()) / 2.0f;
        DrawScope.drawCircle-VaOC9Bg$default($this$Canvas, ColorKt.Color(4293467747L), r * 0.72f, 0L, 0.0f, new Stroke($this$Canvas.toPx-0680j_4(Dp.constructor-impl(2)), 0.0f, 0, 0, (PathEffect) null, 30, (DefaultConstructorMarker) null), (ColorFilter) null, 0, 108, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$529$lambda$515$lambda$512$lambda$511(MutableState $showAutoModal$delegate) {
        LiveActiveDhikrRoom$lambda$425($showAutoModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$529$lambda$518$lambda$517$lambda$516(boolean $isSpecialCampaign, long $minTapIntervalMs, HapticFeedback $haptic, MutableLongState $lastTapTimestamp$delegate, int $maxPerHour, MutableLongState $hourBucketStart$delegate, MutableIntState $hourlyCount$delegate, MutableState $cheatWarningShown$delegate, MutableIntState $pendingCount$delegate, MutableIntState $sessionCount$delegate, MutableLongState $liveTotal$delegate, MutableState $isVibrateOn$delegate, MutableState $isSoundOn$delegate, MutableState $isAutoRunning$delegate) {
        LiveActiveDhikrRoom$doCountStep($isSpecialCampaign, $minTapIntervalMs, $haptic, $lastTapTimestamp$delegate, $maxPerHour, $hourBucketStart$delegate, $hourlyCount$delegate, $cheatWarningShown$delegate, $pendingCount$delegate, $sessionCount$delegate, $liveTotal$delegate, $isVibrateOn$delegate, $isSoundOn$delegate, $isAutoRunning$delegate, 1);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$529$lambda$528$lambda$520$lambda$519(MutableState $showMarbleGalleryModal$delegate) {
        LiveActiveDhikrRoom$lambda$440($showMarbleGalleryModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$529$lambda$528$lambda$523$lambda$522(MutableState $isBeadsViewMode$delegate) {
        LiveActiveDhikrRoom$lambda$434($isBeadsViewMode$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$529$lambda$528$lambda$525$lambda$524(MutableState $showMarbleGalleryModal$delegate) {
        LiveActiveDhikrRoom$lambda$440($showMarbleGalleryModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$529$lambda$528$lambda$527$lambda$526(boolean $isSpecialCampaign, long $minTapIntervalMs, HapticFeedback $haptic, MutableLongState $lastTapTimestamp$delegate, int $maxPerHour, MutableLongState $hourBucketStart$delegate, MutableIntState $hourlyCount$delegate, MutableState $cheatWarningShown$delegate, MutableIntState $pendingCount$delegate, MutableIntState $sessionCount$delegate, MutableLongState $liveTotal$delegate, MutableState $isVibrateOn$delegate, MutableState $isSoundOn$delegate, MutableState $isAutoRunning$delegate) {
        LiveActiveDhikrRoom$doCountStep($isSpecialCampaign, $minTapIntervalMs, $haptic, $lastTapTimestamp$delegate, $maxPerHour, $hourBucketStart$delegate, $hourlyCount$delegate, $cheatWarningShown$delegate, $pendingCount$delegate, $sessionCount$delegate, $liveTotal$delegate, $isVibrateOn$delegate, $isSoundOn$delegate, $isAutoRunning$delegate, 1);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$531$lambda$530(MutableState $leaderboardBarCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $leaderboardBarCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$533$lambda$532(MutableState $showAdPrompt$delegate) {
        LiveActiveDhikrRoom$lambda$397($showAdPrompt$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$540$lambda$539(MutableState $submitBtnCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $submitBtnCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$547$lambda$542$lambda$541(int $maxBatchSize, long $sessionStartMs, CoroutineScope $coroutineScope, MutableIntState $pendingCount$delegate, MutableState $isSubmitting$delegate, GroupCampaignRepository $repository, CampaignItem $campaign, boolean $isSpecialCampaign, MutableLongState $liveTotal$delegate) {
        LiveActiveDhikrRoom$submitBatch($maxBatchSize, $sessionStartMs, $coroutineScope, $pendingCount$delegate, $isSubmitting$delegate, $repository, $campaign, $isSpecialCampaign, $liveTotal$delegate);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0a98  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0aa4  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0add  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0bd1  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0c61  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0c6d  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0ca6  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0da3  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0e33  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0e3f  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0e78  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0fb9  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x1049  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x1055  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x108e  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x119c  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x1226  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x1232  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x126b  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x13c1  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x143a  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x1446  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x147f  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x1559  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x1565  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x159c  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x16d5  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x15b2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:204:0x156b  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x1495  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x144c  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x13cf  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x1281 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:211:0x1238  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x11a9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:215:0x10a4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:216:0x105b  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0fc6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:220:0x0e8e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0e45  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x0db0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0cbc A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0c73  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0bde A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0af3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0aaa  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x0a15  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0927 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:236:0x08de  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0841 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:240:0x07d9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:242:0x0771 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:243:0x0728  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x064e  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x0571 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0528  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x0354 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:251:0x030b  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01e8  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x02f9  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0305  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x033e  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03fe  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0516  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0522  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x055b  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x064b  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0716  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0722  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x075b  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x07ca  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0833  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x08cc  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x08d8  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0911  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0a08  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$576(boolean r133, final com.tasbih.digitaltasbih.data.groups.CampaignItem r134, com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository r135, final android.content.Context r136, final java.lang.String r137, final androidx.compose.runtime.MutableState r138, androidx.compose.foundation.layout.ColumnScope r139, androidx.compose.runtime.Composer r140, int r141) {
        /*
            Method dump skipped, instructions count: 5851
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$576(boolean, com.tasbih.digitaltasbih.data.groups.CampaignItem, com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository, android.content.Context, java.lang.String, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$576$lambda$575$lambda$570$lambda$552$lambda$551(Context $context, CampaignItem $campaign, boolean $isRoomCreator, String $userLang) {
        GroupInviteHelper.INSTANCE.sharePlatform($context, "whatsapp", $campaign, $isRoomCreator, $userLang);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$576$lambda$575$lambda$570$lambda$555$lambda$554(Context $context, CampaignItem $campaign, boolean $isRoomCreator, String $userLang) {
        GroupInviteHelper.INSTANCE.sharePlatform($context, "sms", $campaign, $isRoomCreator, $userLang);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$576$lambda$575$lambda$570$lambda$558$lambda$557(Context $context, CampaignItem $campaign, boolean $isRoomCreator, String $userLang) {
        GroupInviteHelper.INSTANCE.sharePlatform($context, "twitter", $campaign, $isRoomCreator, $userLang);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$576$lambda$575$lambda$570$lambda$561$lambda$560(Context $context, CampaignItem $campaign, boolean $isRoomCreator, String $userLang) {
        GroupInviteHelper.INSTANCE.sharePlatform($context, "facebook", $campaign, $isRoomCreator, $userLang);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$576$lambda$575$lambda$570$lambda$564$lambda$563(Context $context, CampaignItem $campaign, boolean $isRoomCreator, String $userLang) {
        GroupInviteHelper.INSTANCE.sharePlatform($context, "instagram", $campaign, $isRoomCreator, $userLang);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$576$lambda$575$lambda$570$lambda$568$lambda$567(CampaignItem $campaign, boolean $isRoomCreator, String $userLang, Context $context) {
        String codeOrId;
        boolean isPriv = Intrinsics.areEqual($campaign.getVisibility(), "private");
        if (isPriv) {
            String joinCode = $campaign.getJoinCode();
            if (StringsKt.isBlank(joinCode)) {
                joinCode = $campaign.getCampaignId();
            }
            codeOrId = joinCode;
        } else {
            codeOrId = $campaign.getCampaignId();
        }
        String deepLink = GroupInviteHelper.INSTANCE.getDeepLinkUrl(codeOrId);
        if ($isRoomCreator) {
            String fullInvite = GroupInviteHelper.INSTANCE.buildCreatorInviteMessage($campaign, $userLang);
            String toastMsg = isPriv ? "Full Invite & Code Copied!" : "Group Invite Copied!";
            GroupInviteHelper.INSTANCE.copyToClipboard($context, fullInvite, toastMsg);
        } else {
            GroupInviteHelper.INSTANCE.copyToClipboard($context, deepLink, "1-Click Auto Join link copied!");
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$576$lambda$575$lambda$572$lambda$571(MutableState $showShareModal$delegate) {
        LiveActiveDhikrRoom$lambda$391($showShareModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$579$lambda$578(MutableState $isAutoRunning$delegate) {
        LiveActiveDhikrRoom$lambda$419($isAutoRunning$delegate, !LiveActiveDhikrRoom$lambda$418($isAutoRunning$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$581$lambda$580(MutableLongState $autoSpeedMs$delegate, long speed) {
        $autoSpeedMs$delegate.setLongValue(speed);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$583$lambda$582(MutableState $showAutoModal$delegate) {
        LiveActiveDhikrRoom$lambda$425($showAutoModal$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$585$lambda$584(MutableState $activeSkin$delegate, MutableState $isBeadsViewMode$delegate, MutableState $showMarbleGalleryModal$delegate, BeadMarbleSkin chosenSkin) {
        Intrinsics.checkNotNullParameter(chosenSkin, "chosenSkin");
        $activeSkin$delegate.setValue(chosenSkin);
        TasbihRepository.INSTANCE.setActiveBeadSkin(chosenSkin.getId());
        LiveActiveDhikrRoom$lambda$434($isBeadsViewMode$delegate, true);
        LiveActiveDhikrRoom$lambda$440($showMarbleGalleryModal$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$587$lambda$586(MutableState $showMarbleGalleryModal$delegate) {
        LiveActiveDhikrRoom$lambda$440($showMarbleGalleryModal$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$589$lambda$588(Function0 $onDismissShareModal, MutableState $showShareModal$delegate) {
        LiveActiveDhikrRoom$lambda$391($showShareModal$delegate, false);
        $onDismissShareModal.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$592$lambda$591(Activity $activity, MutableState $showAdPrompt$delegate, final MutableState $showLeaderboardModal$delegate) {
        LiveActiveDhikrRoom$lambda$397($showAdPrompt$delegate, false);
        if ($activity != null) {
            AdMobRewardedManager.INSTANCE.showAd($activity, "Group Reciters Leaderboard", new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda21
                public final Object invoke() {
                    return LiveDhikrScreenKt.LiveActiveDhikrRoom$lambda$610$lambda$592$lambda$591$lambda$590($showLeaderboardModal$delegate);
                }
            });
        } else {
            LiveActiveDhikrRoom$lambda$394($showLeaderboardModal$delegate, true);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$592$lambda$591$lambda$590(MutableState $showLeaderboardModal$delegate) {
        LiveActiveDhikrRoom$lambda$394($showLeaderboardModal$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$594$lambda$593(MutableState $showAdPrompt$delegate) {
        LiveActiveDhikrRoom$lambda$397($showAdPrompt$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$596$lambda$595(MutableState $showLeaderboardModal$delegate) {
        LiveActiveDhikrRoom$lambda$394($showLeaderboardModal$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$598$lambda$597(MutableState $showFazilatModal$delegate) {
        LiveActiveDhikrRoom$lambda$400($showFazilatModal$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$601$lambda$600(Context $context, final CampaignItem $campaign, final CoroutineScope $coroutineScope, MutableState $showDeleteConfirmModal$delegate, final GroupCampaignRepository $repository, final Function0 $onBack) {
        LiveActiveDhikrRoom$lambda$403($showDeleteConfirmModal$delegate, false);
        Activity act = findActivity($context);
        if (act == null) {
            act = $context instanceof Activity ? (Activity) $context : null;
        }
        if (act != null) {
            AdMobRewardedManager.INSTANCE.showAd(act, "Delete Group: " + $campaign.getTitle(), new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda67
                public final Object invoke() {
                    return LiveDhikrScreenKt.LiveActiveDhikrRoom$lambda$610$lambda$601$lambda$600$lambda$599($coroutineScope, $repository, $campaign, $onBack);
                }
            });
        } else {
            BuildersKt.launch$default($coroutineScope, (CoroutineContext) null, (CoroutineStart) null, new LiveActiveDhikrRoom.9.12.1.2($repository, $campaign, $onBack, (Continuation) null), 3, (Object) null);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$601$lambda$600$lambda$599(CoroutineScope $coroutineScope, GroupCampaignRepository $repository, CampaignItem $campaign, Function0 $onBack) {
        BuildersKt.launch$default($coroutineScope, (CoroutineContext) null, (CoroutineStart) null, new LiveActiveDhikrRoom.9.12.1.1.1($repository, $campaign, $onBack, (Continuation) null), 3, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$603$lambda$602(MutableState $showDeleteConfirmModal$delegate) {
        LiveActiveDhikrRoom$lambda$403($showDeleteConfirmModal$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$605$lambda$604(GroupCampaignRepository $repository, MutableIntState $roomTourStep$delegate, MutableState $isRoomTourOpen$delegate) {
        if (LiveActiveDhikrRoom$lambda$367($roomTourStep$delegate) >= 5) {
            $repository.setRoomTourDone(true);
            LiveActiveDhikrRoom$lambda$365($isRoomTourOpen$delegate, false);
        } else {
            $roomTourStep$delegate.setIntValue(LiveActiveDhikrRoom$lambda$367($roomTourStep$delegate) + 1);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$607$lambda$606(GroupCampaignRepository $repository, MutableState $isRoomTourOpen$delegate) {
        $repository.setRoomTourDone(true);
        LiveActiveDhikrRoom$lambda$365($isRoomTourOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiveActiveDhikrRoom$lambda$610$lambda$609$lambda$608(GroupCampaignRepository $repository, MutableState $isRoomTourOpen$delegate) {
        $repository.setRoomTourDone(true);
        LiveActiveDhikrRoom$lambda$365($isRoomTourOpen$delegate, false);
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final void GroupShareModal(@NotNull final CampaignItem campaign, final boolean isCreator, boolean isDarkTheme, @Nullable String selectedLanguage, @NotNull final Function0<Unit> function0, @Nullable Composer $composer, final int $changed, final int i) {
        final boolean z;
        boolean z2;
        String str;
        boolean isDarkTheme2;
        final String selectedLanguage2;
        String codeOrId;
        Object deepLinkUrl;
        Composer $composer2;
        final boolean isDarkTheme3;
        final String selectedLanguage3;
        int i2;
        Intrinsics.checkNotNullParameter(campaign, "campaign");
        Intrinsics.checkNotNullParameter(function0, "onDismiss");
        Composer $composer3 = $composer.startRestartGroup(411673385);
        ComposerKt.sourceInformation($composer3, "C(GroupShareModal)P(!3,4)3270@145281L7,3271@145326L7,3274@145515L65,3282@145954L20913,3282@145917L20950:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(campaign) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            z = isCreator;
            $dirty |= $composer3.changed(z) ? 32 : 16;
        } else {
            z = isCreator;
        }
        int i3 = i & 4;
        if (i3 != 0) {
            $dirty |= 384;
            z2 = isDarkTheme;
        } else if (($changed & 384) == 0) {
            z2 = isDarkTheme;
            $dirty |= $composer3.changed(z2) ? 256 : 128;
        } else {
            z2 = isDarkTheme;
        }
        if (($changed & 3072) == 0) {
            if ((i & 8) == 0) {
                str = selectedLanguage;
                if ($composer3.changed(str)) {
                    i2 = 2048;
                    $dirty |= i2;
                }
            } else {
                str = selectedLanguage;
            }
            i2 = 1024;
            $dirty |= i2;
        } else {
            str = selectedLanguage;
        }
        if (($changed & 24576) == 0) {
            $dirty |= $composer3.changedInstance(function0) ? 16384 : 8192;
        }
        if (($dirty & 9363) == 9362 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
            isDarkTheme3 = z2;
            selectedLanguage3 = str;
        } else {
            $composer3.startDefaults();
            if (($changed & 1) == 0 || $composer3.getDefaultsInvalid()) {
                isDarkTheme2 = i3 != 0 ? false : z2;
                if ((i & 8) != 0) {
                    $dirty &= -7169;
                    selectedLanguage2 = PrayerRepository.INSTANCE.getLanguage();
                } else {
                    selectedLanguage2 = str;
                }
            } else {
                $composer3.skipToGroupEnd();
                if ((i & 8) != 0) {
                    $dirty &= -7169;
                }
                isDarkTheme2 = z2;
                selectedLanguage2 = str;
            }
            $composer3.endDefaults();
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(411673385, $dirty, -1, "com.tasbih.digitaltasbih.ui.screens.groups.GroupShareModal (LiveDhikrScreen.kt:3269)");
            }
            CompositionLocal localContext = AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart($composer3, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume = $composer3.consume(localContext);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final Context context = (Context) consume;
            CompositionLocal localHapticFeedback = CompositionLocalsKt.getLocalHapticFeedback();
            ComposerKt.sourceInformationMarkerStart($composer3, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume2 = $composer3.consume(localHapticFeedback);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final HapticFeedback haptic = (HapticFeedback) consume2;
            boolean isPrivate = Intrinsics.areEqual(campaign.getVisibility(), "private");
            if (isPrivate) {
                String joinCode = campaign.getJoinCode();
                if (StringsKt.isBlank(joinCode)) {
                    joinCode = campaign.getCampaignId();
                }
                codeOrId = joinCode;
            } else {
                codeOrId = campaign.getCampaignId();
            }
            ComposerKt.sourceInformationMarkerStart($composer3, 481147082, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            boolean changed = $composer3.changed(codeOrId);
            Object rememberedValue = $composer3.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                deepLinkUrl = GroupInviteHelper.INSTANCE.getDeepLinkUrl(codeOrId);
                $composer3.updateRememberedValue(deepLinkUrl);
            } else {
                deepLinkUrl = rememberedValue;
            }
            final String deepLink = (String) deepLinkUrl;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            long bgCard = isDarkTheme2 ? ColorKt.Color(4278657304L) : Color.Companion.getWhite-0d7_KjU();
            long textColor = isDarkTheme2 ? Color.Companion.getWhite-0d7_KjU() : ColorKt.Color(4278460694L);
            final long subColor = ColorKt.Color(isDarkTheme2 ? 4287931320L : 4282865001L);
            final long cardBorder = ColorKt.Color(isDarkTheme2 ? 4278926123L : 4293060848L);
            final long gold = ColorKt.Color(4292128567L);
            final boolean isDarkTheme4 = isDarkTheme2;
            final long bgCard2 = bgCard;
            final long textColor2 = textColor;
            AndroidDialog_androidKt.Dialog(function0, (DialogProperties) null, ComposableLambdaKt.rememberComposableLambda(4402112, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda85
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.GroupShareModal$lambda$652(bgCard2, isDarkTheme4, campaign, z, textColor2, subColor, cardBorder, function0, gold, haptic, context, deepLink, selectedLanguage2, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), $composer3, (($dirty >> 12) & 14) | 384, 2);
            $composer2 = $composer3;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            isDarkTheme3 = isDarkTheme4;
            selectedLanguage3 = selectedLanguage2;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda86
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.GroupShareModal$lambda$653(campaign, isCreator, isDarkTheme3, selectedLanguage3, function0, $changed, i, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit GroupShareModal$lambda$652(long $bgCard, final boolean $isDarkTheme, final CampaignItem $campaign, final boolean $isCreator, final long $textColor, final long $subColor, final long $cardBorder, final Function0 $onDismiss, final long $gold, final HapticFeedback $haptic, final Context $context, final String $deepLink, final String $selectedLanguage, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C3288@146159L35,3290@146335L39,3291@146385L20476,3283@145964L20897:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(4402112, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.GroupShareModal.<anonymous> (LiveDhikrScreen.kt:3283)");
            }
            CardKt.Card(PaddingKt.padding-VpY3zN4$default(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, (Object) null), 0.0f, Dp.constructor-impl(12), 1, (Object) null), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(26)), CardDefaults.INSTANCE.cardColors-ro_MJ88($bgCard, 0L, 0L, 0L, $composer, CardDefaults.$stable << 12, 14), CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl(12), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, $composer, (CardDefaults.$stable << 18) | 6, 62), BorderStrokeKt.BorderStroke-cXLIe8U(Dp.constructor-impl((float) 1.5d), ColorKt.Color($isDarkTheme ? 4279286145L : 4278669885L)), ComposableLambdaKt.rememberComposableLambda(1820602418, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda61
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return LiveDhikrScreenKt.GroupShareModal$lambda$652$lambda$651($campaign, $isCreator, $textColor, $subColor, $isDarkTheme, $cardBorder, $onDismiss, $gold, $haptic, $context, $deepLink, $selectedLanguage, (ColumnScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer, 54), $composer, 196614, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0bd6  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0cd7  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0ce3  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0d1c  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0dec  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0e5f  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0e92  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0f68  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x1174  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x1180  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x12a3  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x12af  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x12e8  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x13a4  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x145d  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x1512  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x15b8  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x15c4  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x15fd  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x16d8  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x16e4  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x171d  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x1808  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x1814  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x184d  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x1935  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x1941  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x197a  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x1a2e  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x1b3b  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x1ba5  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x1bb7  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x1d13  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x1d1f  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x1d58  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x1e0d  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x1e8c  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0232  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x1f11  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x1ff8  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x2004  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x203d  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x20ec  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x21df  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x225c  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x023e  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x22de  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x235a  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x2366  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x239f  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x2478  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x2484  */
    /* JADX WARN: Removed duplicated region for block: B:312:0x24bb  */
    /* JADX WARN: Removed duplicated region for block: B:317:0x2530  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x2586  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x26c9  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x25a0  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x2536  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x24d1  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x248a  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x23b5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:331:0x236c  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x22e6  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x226a  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x21f0  */
    /* JADX WARN: Removed duplicated region for block: B:338:0x2115  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x2053 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:341:0x200a  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x1f21  */
    /* JADX WARN: Removed duplicated region for block: B:345:0x1e9c  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x1e1d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:349:0x1d6e  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x1d25  */
    /* JADX WARN: Removed duplicated region for block: B:351:0x1bbd  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x1baa  */
    /* JADX WARN: Removed duplicated region for block: B:354:0x1a34  */
    /* JADX WARN: Removed duplicated region for block: B:356:0x1990 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:357:0x1947  */
    /* JADX WARN: Removed duplicated region for block: B:359:0x1863 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:360:0x181a  */
    /* JADX WARN: Removed duplicated region for block: B:362:0x1733  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x16ea  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x1613 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:366:0x15ca  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x151a  */
    /* JADX WARN: Removed duplicated region for block: B:369:0x146b  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x02de  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x13aa  */
    /* JADX WARN: Removed duplicated region for block: B:372:0x12fe A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:373:0x12b5  */
    /* JADX WARN: Removed duplicated region for block: B:376:0x1186  */
    /* JADX WARN: Removed duplicated region for block: B:382:0x0f19  */
    /* JADX WARN: Removed duplicated region for block: B:385:0x0d32 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:386:0x0ce9  */
    /* JADX WARN: Removed duplicated region for block: B:387:0x0c42  */
    /* JADX WARN: Removed duplicated region for block: B:392:0x09f9  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x034d  */
    /* JADX WARN: Removed duplicated region for block: B:411:0x07e2  */
    /* JADX WARN: Removed duplicated region for block: B:413:0x0779 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:414:0x0730  */
    /* JADX WARN: Removed duplicated region for block: B:416:0x0652 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:417:0x0609  */
    /* JADX WARN: Removed duplicated region for block: B:419:0x052f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:420:0x04e6  */
    /* JADX WARN: Removed duplicated region for block: B:421:0x0436  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x03ab  */
    /* JADX WARN: Removed duplicated region for block: B:425:0x0357  */
    /* JADX WARN: Removed duplicated region for block: B:426:0x02e1  */
    /* JADX WARN: Removed duplicated region for block: B:429:0x0244  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x039f  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0430  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x04d4  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x04e0  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0519  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x05f7  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0603  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x063c  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x071e  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x072a  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0763  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x07db  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0839  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit GroupShareModal$lambda$652$lambda$651(final com.tasbih.digitaltasbih.data.groups.CampaignItem r166, final boolean r167, long r168, long r170, boolean r172, long r173, kotlin.jvm.functions.Function0 r175, long r176, final androidx.compose.ui.hapticfeedback.HapticFeedback r178, final android.content.Context r179, final java.lang.String r180, final java.lang.String r181, androidx.compose.foundation.layout.ColumnScope r182, androidx.compose.runtime.Composer r183, int r184) {
        /*
            Method dump skipped, instructions count: 9935
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.GroupShareModal$lambda$652$lambda$651(com.tasbih.digitaltasbih.data.groups.CampaignItem, boolean, long, long, boolean, long, kotlin.jvm.functions.Function0, long, androidx.compose.ui.hapticfeedback.HapticFeedback, android.content.Context, java.lang.String, java.lang.String, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GroupShareModal$lambda$652$lambda$651$lambda$650$lambda$626$lambda$625$lambda$624$lambda$623(HapticFeedback $haptic, Context $context, CampaignItem $campaign) {
        $haptic.performHapticFeedback-CdsT49E(HapticFeedbackType.Companion.getLongPress-5zf0vsI());
        GroupInviteHelper.INSTANCE.copyToClipboard($context, $campaign.getJoinCode(), "Invite Code Copied: " + $campaign.getJoinCode());
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GroupShareModal$lambda$652$lambda$651$lambda$650$lambda$632$lambda$631$lambda$630$lambda$629$lambda$628(HapticFeedback $haptic, Context $context, String $deepLink) {
        $haptic.performHapticFeedback-CdsT49E(HapticFeedbackType.Companion.getLongPress-5zf0vsI());
        GroupInviteHelper.INSTANCE.copyToClipboard($context, $deepLink, "Auto-Join Link Copied!");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GroupShareModal$lambda$652$lambda$651$lambda$650$lambda$639$lambda$634$lambda$633(boolean $isCreator, CampaignItem $campaign, String $selectedLanguage, Context $context) {
        String text = $isCreator ? GroupInviteHelper.INSTANCE.buildCreatorInviteMessage($campaign, $selectedLanguage) : GroupInviteHelper.INSTANCE.buildMemberInviteMessage($campaign, $selectedLanguage);
        GroupInviteHelper.INSTANCE.shareToWhatsApp($context, text);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GroupShareModal$lambda$652$lambda$651$lambda$650$lambda$639$lambda$636$lambda$635(boolean $isCreator, CampaignItem $campaign, String $selectedLanguage, Context $context) {
        String text = $isCreator ? GroupInviteHelper.INSTANCE.buildCreatorInviteMessage($campaign, $selectedLanguage) : GroupInviteHelper.INSTANCE.buildMemberInviteMessage($campaign, $selectedLanguage);
        GroupInviteHelper.INSTANCE.shareToSms($context, text);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GroupShareModal$lambda$652$lambda$651$lambda$650$lambda$639$lambda$638$lambda$637(boolean $isCreator, CampaignItem $campaign, String $selectedLanguage, Context $context) {
        String text = $isCreator ? GroupInviteHelper.INSTANCE.buildCreatorInviteMessage($campaign, $selectedLanguage) : GroupInviteHelper.INSTANCE.buildMemberInviteMessage($campaign, $selectedLanguage);
        GroupInviteHelper.INSTANCE.shareToTwitter($context, text);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GroupShareModal$lambda$652$lambda$651$lambda$650$lambda$646$lambda$641$lambda$640(boolean $isCreator, CampaignItem $campaign, String $selectedLanguage, Context $context, String $deepLink) {
        String text = $isCreator ? GroupInviteHelper.INSTANCE.buildCreatorInviteMessage($campaign, $selectedLanguage) : GroupInviteHelper.INSTANCE.buildMemberInviteMessage($campaign, $selectedLanguage);
        GroupInviteHelper.INSTANCE.shareToFacebook($context, text, $deepLink);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GroupShareModal$lambda$652$lambda$651$lambda$650$lambda$646$lambda$643$lambda$642(boolean $isCreator, CampaignItem $campaign, String $selectedLanguage, Context $context) {
        String text = $isCreator ? GroupInviteHelper.INSTANCE.buildCreatorInviteMessage($campaign, $selectedLanguage) : GroupInviteHelper.INSTANCE.buildMemberInviteMessage($campaign, $selectedLanguage);
        GroupInviteHelper.INSTANCE.shareToInstagram($context, text);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GroupShareModal$lambda$652$lambda$651$lambda$650$lambda$646$lambda$645$lambda$644(Context $context, CampaignItem $campaign, boolean $isCreator, String $selectedLanguage) {
        GroupInviteHelper.INSTANCE.shareCampaign($context, $campaign, $isCreator, $selectedLanguage);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit GroupShareModal$lambda$652$lambda$651$lambda$650$lambda$649(boolean $isCreator, RowScope $this$Button, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C3694@166564L255:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1706074344, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.GroupShareModal.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:3694)");
            }
            TextKt.Text--4IGK_g($isCreator ? "Start Reciting in Group 📿" : "Done", (Modifier) null, Color.Companion.getWhite-0d7_KjU(), TextUnitKt.getSp(14), (FontStyle) null, FontWeight.Companion.getBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 200064, 0, 131026);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:74:0x0314  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0320  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x04ad  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0326  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /* renamed from: SharePlatformTile-FU0evQE, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static final void m1SharePlatformTileFU0evQE(final java.lang.String r77, final java.lang.String r78, long r79, androidx.compose.ui.graphics.Brush r81, androidx.compose.ui.Modifier r82, final kotlin.jvm.functions.Function0<kotlin.Unit> r83, androidx.compose.runtime.Composer r84, final int r85, final int r86) {
        /*
            Method dump skipped, instructions count: 1232
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.m1SharePlatformTileFU0evQE(java.lang.String, java.lang.String, long, androidx.compose.ui.graphics.Brush, androidx.compose.ui.Modifier, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SharePlatformTile_FU0evQE$lambda$655$lambda$654(Function0 $onClick) {
        $onClick.invoke();
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    private static final void ShareIconBtn(final String platform, final Function0<Unit> function0, Composer $composer, final int $changed) {
        Composer $composer2 = $composer.startRestartGroup(-436555241);
        ComposerKt.sourceInformation($composer2, "C(ShareIconBtn)P(1)3750@168290L71,3745@168054L307:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changed(platform) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer2.changedInstance(function0) ? 32 : 16;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-436555241, $dirty2, -1, "com.tasbih.digitaltasbih.ui.screens.groups.ShareIconBtn (LiveDhikrScreen.kt:3744)");
            }
            ButtonKt.OutlinedButton(function0, (Modifier) null, false, RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(10)), (ButtonColors) null, (ButtonElevation) null, BorderStrokeKt.BorderStroke-cXLIe8U(Dp.constructor-impl(1), Color.copy-wmQWz5c$default(LuxuryGold, 0.4f, 0.0f, 0.0f, 0.0f, 14, (Object) null)), PaddingKt.PaddingValues-YgX7TsA(Dp.constructor-impl(12), Dp.constructor-impl(4)), (MutableInteractionSource) null, ComposableLambdaKt.rememberComposableLambda(-1358029687, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda253
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return LiveDhikrScreenKt.ShareIconBtn$lambda$659(platform, (RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer2, 54), $composer2, (($dirty2 >> 3) & 14) | 819462144, 310);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda254
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.ShareIconBtn$lambda$660(platform, function0, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit ShareIconBtn$lambda$659(String $platform, RowScope $this$OutlinedButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$OutlinedButton, "$this$OutlinedButton");
        ComposerKt.sourceInformation($composer, "C3751@168300L55:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1358029687, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.ShareIconBtn.<anonymous> (LiveDhikrScreen.kt:3751)");
            }
            TextKt.Text--4IGK_g($platform, (Modifier) null, ParchmentGold, TextUnitKt.getSp(11), (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 3456, 0, 131058);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x0525  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x05a0  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0531  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x04df  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x046a  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x03c1  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x037a  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0294  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x024d  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x023b  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0247  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0280  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0368  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0374  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x03ab  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0414  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x04da  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static final void PillButton(final java.lang.String r101, final java.lang.String r102, final androidx.compose.ui.graphics.vector.ImageVector r103, final boolean r104, boolean r105, final kotlin.jvm.functions.Function0<kotlin.Unit> r106, androidx.compose.runtime.Composer r107, final int r108, final int r109) {
        /*
            Method dump skipped, instructions count: 1470
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.PillButton(java.lang.String, java.lang.String, androidx.compose.ui.graphics.vector.ImageVector, boolean, boolean, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PillButton$lambda$662$lambda$661(Function0 $onClick) {
        $onClick.invoke();
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    /* renamed from: PulsingLiveDot-Iv8Zu3U, reason: not valid java name */
    private static final void m0PulsingLiveDotIv8Zu3U(long color, Composer $composer, final int $changed, final int i) {
        long j;
        final long color2;
        Composer $composer2 = $composer.startRestartGroup(-1082253068);
        ComposerKt.sourceInformation($composer2, "C(PulsingLiveDot)P(0:c#ui.graphics.Color)3833@170660L43,3834@170732L264,3843@171001L144:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        int i2 = i & 1;
        if (i2 != 0) {
            $dirty |= 6;
            j = color;
        } else if (($changed & 6) == 0) {
            j = color;
            $dirty |= $composer2.changed(j) ? 4 : 2;
        } else {
            j = color;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 3) == 2 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
            color2 = j;
        } else {
            if (i2 != 0) {
                color2 = ColorKt.Color(4293870660L);
            } else {
                color2 = j;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1082253068, $dirty2, -1, "com.tasbih.digitaltasbih.ui.screens.groups.PulsingLiveDot (LiveDhikrScreen.kt:3832)");
            }
            InfiniteTransition transition = InfiniteTransitionKt.rememberInfiniteTransition("pulse", $composer2, 6, 0);
            State alpha$delegate = InfiniteTransitionKt.animateFloat(transition, 0.35f, 1.0f, AnimationSpecKt.infiniteRepeatable-9IiC70o$default(AnimationSpecKt.tween$default(600, 0, EasingKt.getFastOutSlowInEasing(), 2, (Object) null), RepeatMode.Reverse, 0L, 4, (Object) null), "alpha", $composer2, InfiniteTransition.$stable | 25008 | (InfiniteRepeatableSpec.$stable << 9), 0);
            BoxKt.Box(BackgroundKt.background-bw27NRU$default(ClipKt.clip(SizeKt.size-3ABfNKs(Modifier.Companion, Dp.constructor-impl(8)), RoundedCornerShapeKt.getCircleShape()), Color.copy-wmQWz5c$default(color2, PulsingLiveDot_Iv8Zu3U$lambda$666(alpha$delegate), 0.0f, 0.0f, 0.0f, 14, (Object) null), (Shape) null, 2, (Object) null), $composer2, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda165
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.PulsingLiveDot_Iv8Zu3U$lambda$667(color2, $changed, i, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    private static final float PulsingLiveDot_Iv8Zu3U$lambda$666(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    private static final void UmrahMegaRegisterModal(final CampaignItem campaign, final GroupCampaignRepository repository, boolean isDarkTheme, final Function0<Unit> function0, final Function1<? super StoredMegaTicket, Unit> function1, Composer $composer, final int $changed, final int i) {
        GroupCampaignRepository groupCampaignRepository;
        boolean z;
        Function1<? super StoredMegaTicket, Unit> function12;
        boolean isDarkTheme2;
        Object obj;
        CoroutineScope coroutineScope;
        Object obj2;
        Context context;
        Object obj3;
        Object obj4;
        Object obj5;
        Object obj6;
        Object obj7;
        Object obj8;
        Composer $composer2;
        final boolean isDarkTheme3;
        Composer $composer3 = $composer.startRestartGroup(-1336189780);
        ComposerKt.sourceInformation($composer3, "C(UmrahMegaRegisterModal)P(!1,4)3860@171501L7,3861@171546L7,3862@171579L24,3863@171620L53,3864@171691L31,3865@171739L53,3866@171817L33,3867@171877L59,3868@171961L34,3870@172021L34,3871@172083L42,4120@185008L3357,4194@188391L248,3880@172488L834,3900@173339L11643,3878@172421L16302:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(campaign) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            groupCampaignRepository = repository;
            $dirty |= $composer3.changedInstance(groupCampaignRepository) ? 32 : 16;
        } else {
            groupCampaignRepository = repository;
        }
        int i2 = i & 4;
        if (i2 != 0) {
            $dirty |= 384;
            z = isDarkTheme;
        } else if (($changed & 384) == 0) {
            z = isDarkTheme;
            $dirty |= $composer3.changed(z) ? 256 : 128;
        } else {
            z = isDarkTheme;
        }
        if (($changed & 3072) == 0) {
            $dirty |= $composer3.changedInstance(function0) ? 2048 : 1024;
        }
        if (($changed & 24576) == 0) {
            function12 = function1;
            $dirty |= $composer3.changedInstance(function12) ? 16384 : 8192;
        } else {
            function12 = function1;
        }
        if (($dirty & 9363) == 9362 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
            isDarkTheme3 = z;
        } else {
            if (i2 != 0) {
                isDarkTheme2 = false;
            } else {
                isDarkTheme2 = z;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1336189780, $dirty, -1, "com.tasbih.digitaltasbih.ui.screens.groups.UmrahMegaRegisterModal (LiveDhikrScreen.kt:3859)");
            }
            CompositionLocal localContext = AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart($composer3, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume = $composer3.consume(localContext);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            Context context2 = (Context) consume;
            CompositionLocal localHapticFeedback = CompositionLocalsKt.getLocalHapticFeedback();
            ComposerKt.sourceInformationMarkerStart($composer3, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume2 = $composer3.consume(localHapticFeedback);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final HapticFeedback haptic = (HapticFeedback) consume2;
            ComposerKt.sourceInformationMarkerStart($composer3, 773894976, "CC(rememberCoroutineScope)482@20332L144:Effects.kt#9igjgp");
            ComposerKt.sourceInformationMarkerStart($composer3, -954367824, "CC(remember):Effects.kt#9igjgp");
            Object rememberedValue = $composer3.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new CompositionScopedCoroutineScopeCanceller(EffectsKt.createCompositionCoroutineScope(EmptyCoroutineContext.INSTANCE, $composer3));
                $composer3.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer3);
            CoroutineScope coroutineScope2 = ((CompositionScopedCoroutineScopeCanceller) obj).getCoroutineScope();
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 116854945, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue2 = $composer3.rememberedValue();
            if (rememberedValue2 == Composer.Companion.getEmpty()) {
                coroutineScope = coroutineScope2;
                obj2 = SnapshotStateKt.mutableStateOf$default(groupCampaignRepository.getUserName(), (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer3.updateRememberedValue(obj2);
            } else {
                coroutineScope = coroutineScope2;
                obj2 = rememberedValue2;
            }
            final MutableState name$delegate = (MutableState) obj2;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 116857195, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue3 = $composer3.rememberedValue();
            if (rememberedValue3 == Composer.Companion.getEmpty()) {
                context = context2;
                obj3 = SnapshotStateKt.mutableStateOf$default("", (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer3.updateRememberedValue(obj3);
            } else {
                context = context2;
                obj3 = rememberedValue3;
            }
            final MutableState phone$delegate = (MutableState) obj3;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 116858753, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue4 = $composer3.rememberedValue();
            if (rememberedValue4 == Composer.Companion.getEmpty()) {
                obj4 = SnapshotStateKt.mutableStateOf$default(groupCampaignRepository.getUserCity(), (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer3.updateRememberedValue(obj4);
            } else {
                obj4 = rememberedValue4;
            }
            final MutableState city$delegate = (MutableState) obj4;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 116861229, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue5 = $composer3.rememberedValue();
            if (rememberedValue5 == Composer.Companion.getEmpty()) {
                obj5 = SnapshotStateKt.mutableStateOf$default(true, (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer3.updateRememberedValue(obj5);
            } else {
                obj5 = rememberedValue5;
            }
            final MutableState agreeChecked$delegate = (MutableState) obj5;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 116863175, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue6 = $composer3.rememberedValue();
            if (rememberedValue6 == Composer.Companion.getEmpty()) {
                obj6 = SnapshotStateKt.mutableStateOf$default(Boolean.valueOf(groupCampaignRepository.isConsentAccepted()), (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer3.updateRememberedValue(obj6);
            } else {
                obj6 = rememberedValue6;
            }
            final MutableState consentChecked$delegate = (MutableState) obj6;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 116865838, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue7 = $composer3.rememberedValue();
            if (rememberedValue7 == Composer.Companion.getEmpty()) {
                obj7 = SnapshotStateKt.mutableStateOf$default(false, (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer3.updateRememberedValue(obj7);
            } else {
                obj7 = rememberedValue7;
            }
            final MutableState isSubmitting$delegate = (MutableState) obj7;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 116867758, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue8 = $composer3.rememberedValue();
            if (rememberedValue8 == Composer.Companion.getEmpty()) {
                rememberedValue8 = SnapshotStateKt.mutableStateOf$default(false, (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer3.updateRememberedValue(rememberedValue8);
            }
            final MutableState consentError$delegate = (MutableState) rememberedValue8;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 116869750, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue9 = $composer3.rememberedValue();
            if (rememberedValue9 == Composer.Companion.getEmpty()) {
                obj8 = SnapshotStateKt.mutableStateOf$default((Object) null, (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer3.updateRememberedValue(obj8);
            } else {
                obj8 = rememberedValue9;
            }
            final MutableState generalErrorMsg$delegate = (MutableState) obj8;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            long dialogBg = isDarkTheme2 ? DarkCardBg : Color.Companion.getWhite-0d7_KjU();
            long Color = isDarkTheme2 ? DarkCardBorder : ColorKt.Color(4278532908L);
            final long textPrimary = isDarkTheme2 ? Color.Companion.getWhite-0d7_KjU() : ColorKt.Color(4279179050L);
            long textSub = isDarkTheme2 ? TextMutedGreen : ColorKt.Color(4283593572L);
            final long textSub2 = textSub;
            int $dirty2 = $dirty;
            final GroupCampaignRepository groupCampaignRepository2 = groupCampaignRepository;
            final boolean isDarkTheme4 = isDarkTheme2;
            final Function1<? super StoredMegaTicket, Unit> function13 = function12;
            final CoroutineScope coroutineScope3 = coroutineScope;
            final Context context3 = context;
            $composer2 = $composer3;
            AndroidAlertDialog_androidKt.AlertDialog-Oix01E0(function0, ComposableLambdaKt.rememberComposableLambda(1575022436, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda74
                public final Object invoke(Object obj9, Object obj10) {
                    return LiveDhikrScreenKt.UmrahMegaRegisterModal$lambda$695(isDarkTheme4, context3, haptic, coroutineScope3, groupCampaignRepository2, campaign, function13, isSubmitting$delegate, generalErrorMsg$delegate, name$delegate, phone$delegate, city$delegate, agreeChecked$delegate, consentChecked$delegate, consentError$delegate, (Composer) obj9, ((Integer) obj10).intValue());
                }
            }, $composer3, 54), (Modifier) null, ComposableLambdaKt.rememberComposableLambda(1327054242, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda75
                public final Object invoke(Object obj9, Object obj10) {
                    return LiveDhikrScreenKt.UmrahMegaRegisterModal$lambda$697(function0, isDarkTheme4, (Composer) obj9, ((Integer) obj10).intValue());
                }
            }, $composer3, 54), (Function2) null, ComposableLambdaKt.rememberComposableLambda(1079086048, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda76
                public final Object invoke(Object obj9, Object obj10) {
                    return LiveDhikrScreenKt.UmrahMegaRegisterModal$lambda$699(isDarkTheme4, campaign, textSub2, (Composer) obj9, ((Integer) obj10).intValue());
                }
            }, $composer3, 54), ComposableLambdaKt.rememberComposableLambda(-1192381697, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda77
                public final Object invoke(Object obj9, Object obj10) {
                    return LiveDhikrScreenKt.UmrahMegaRegisterModal$lambda$726(isDarkTheme4, name$delegate, generalErrorMsg$delegate, phone$delegate, city$delegate, agreeChecked$delegate, textPrimary, consentError$delegate, consentChecked$delegate, (Composer) obj9, ((Integer) obj10).intValue());
                }
            }, $composer3, 54), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(24)), dialogBg, 0L, 0L, 0L, 0.0f, (DialogProperties) null, $composer2, (($dirty2 >> 9) & 14) | 1772592, 0, 15892);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            isDarkTheme3 = isDarkTheme4;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda78
                public final Object invoke(Object obj9, Object obj10) {
                    return LiveDhikrScreenKt.UmrahMegaRegisterModal$lambda$727(campaign, repository, isDarkTheme3, function0, function1, $changed, i, (Composer) obj9, ((Integer) obj10).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String UmrahMegaRegisterModal$lambda$669(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String UmrahMegaRegisterModal$lambda$672(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String UmrahMegaRegisterModal$lambda$675(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final boolean UmrahMegaRegisterModal$lambda$678(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void UmrahMegaRegisterModal$lambda$679(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean UmrahMegaRegisterModal$lambda$681(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void UmrahMegaRegisterModal$lambda$682(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean UmrahMegaRegisterModal$lambda$684(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void UmrahMegaRegisterModal$lambda$685(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean UmrahMegaRegisterModal$lambda$687(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void UmrahMegaRegisterModal$lambda$688(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final String UmrahMegaRegisterModal$lambda$690(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x018d  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0226  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0190  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit UmrahMegaRegisterModal$lambda$699(boolean r51, com.tasbih.digitaltasbih.data.groups.CampaignItem r52, long r53, androidx.compose.runtime.Composer r55, int r56) {
        /*
            Method dump skipped, instructions count: 556
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.UmrahMegaRegisterModal$lambda$699(boolean, com.tasbih.digitaltasbih.data.groups.CampaignItem, long, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x08ba  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x08e0  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0967  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0973  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x09ac  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0a92  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0a9e  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0ad7  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0bbb  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0bc7  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0c00  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0c77  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0cc7  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0d5b  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0d67  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0da0  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0e13  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0e83  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0f10  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0fae  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0fba  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0ff3  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x107d  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x109c  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x1142  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x1168  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x11b5  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x11fb  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x128c  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x1298  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x12d1  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x13ab  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x13b7  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x13f0  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x14ce  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x14da  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x1511  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x1595  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x15b6  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x1610  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x1625  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x1683  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x1741  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x17d1  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x174d  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x1704  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x1631  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x1617  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x15c2  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x15a3  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x1527 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:258:0x14e0  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0189  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x1406 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x13bd  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x12e7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:264:0x129e  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x1207  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x11c3  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x1172  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x114c  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x109f  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x108d  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x1009 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0fc0  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x0f1e  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x0e86  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x0e19  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x0db6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:283:0x0d6d  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x0ccf  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x0c7a  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x0c16 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:288:0x0bcd  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0194  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x0aed A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:291:0x0aa4  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x09c2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0979  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x08e4  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x08c2  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x084e  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x07a3  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x078f  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x0780  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x0775  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x06f5  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x064d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:305:0x0604  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x04e1  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x0441  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x041b  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x03a7  */
    /* JADX WARN: Removed duplicated region for block: B:312:0x02fc  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x02e8  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x02d9  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x02ce  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:317:0x01ba  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x01a6  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x019f  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x018c  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0257  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x02cb  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x02d6  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x02e1  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x02f5  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0395  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0413  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0439  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x04cf  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x04db  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x05f2  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x05fe  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0637  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x06ef  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0772  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x077d  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0788  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x079c  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x083c  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit UmrahMegaRegisterModal$lambda$726(boolean r163, final androidx.compose.runtime.MutableState r164, final androidx.compose.runtime.MutableState r165, final androidx.compose.runtime.MutableState r166, final androidx.compose.runtime.MutableState r167, final androidx.compose.runtime.MutableState r168, long r169, final androidx.compose.runtime.MutableState r171, final androidx.compose.runtime.MutableState r172, androidx.compose.runtime.Composer r173, int r174) {
        /*
            Method dump skipped, instructions count: 6103
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.UmrahMegaRegisterModal$lambda$726(boolean, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, long, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UmrahMegaRegisterModal$lambda$726$lambda$725$lambda$701$lambda$700(MutableState $name$delegate, MutableState $generalErrorMsg$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $name$delegate.setValue(it);
        $generalErrorMsg$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UmrahMegaRegisterModal$lambda$726$lambda$725$lambda$703$lambda$702(MutableState $phone$delegate, MutableState $generalErrorMsg$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $phone$delegate.setValue(it);
        $generalErrorMsg$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UmrahMegaRegisterModal$lambda$726$lambda$725$lambda$707$lambda$706(MutableState $city$delegate, MutableState $generalErrorMsg$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $city$delegate.setValue(it);
        $generalErrorMsg$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UmrahMegaRegisterModal$lambda$726$lambda$725$lambda$713$lambda$712(MutableState $agreeChecked$delegate) {
        UmrahMegaRegisterModal$lambda$679($agreeChecked$delegate, !UmrahMegaRegisterModal$lambda$678($agreeChecked$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UmrahMegaRegisterModal$lambda$726$lambda$725$lambda$716$lambda$715$lambda$714(MutableState $agreeChecked$delegate, boolean it) {
        UmrahMegaRegisterModal$lambda$679($agreeChecked$delegate, it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UmrahMegaRegisterModal$lambda$726$lambda$725$lambda$718$lambda$717(MutableState $consentChecked$delegate, MutableState $consentError$delegate) {
        UmrahMegaRegisterModal$lambda$682($consentChecked$delegate, !UmrahMegaRegisterModal$lambda$681($consentChecked$delegate));
        if (UmrahMegaRegisterModal$lambda$681($consentChecked$delegate)) {
            UmrahMegaRegisterModal$lambda$688($consentError$delegate, false);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UmrahMegaRegisterModal$lambda$726$lambda$725$lambda$723$lambda$722$lambda$721$lambda$720$lambda$719(MutableState $consentChecked$delegate, MutableState $consentError$delegate, boolean it) {
        UmrahMegaRegisterModal$lambda$682($consentChecked$delegate, it);
        if (it) {
            UmrahMegaRegisterModal$lambda$688($consentError$delegate, false);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0119  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit UmrahMegaRegisterModal$lambda$695(final boolean r34, final android.content.Context r35, final androidx.compose.ui.hapticfeedback.HapticFeedback r36, final kotlinx.coroutines.CoroutineScope r37, final com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository r38, final com.tasbih.digitaltasbih.data.groups.CampaignItem r39, final kotlin.jvm.functions.Function1 r40, final androidx.compose.runtime.MutableState r41, final androidx.compose.runtime.MutableState r42, final androidx.compose.runtime.MutableState r43, final androidx.compose.runtime.MutableState r44, final androidx.compose.runtime.MutableState r45, final androidx.compose.runtime.MutableState r46, final androidx.compose.runtime.MutableState r47, final androidx.compose.runtime.MutableState r48, androidx.compose.runtime.Composer r49, int r50) {
        /*
            Method dump skipped, instructions count: 287
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.UmrahMegaRegisterModal$lambda$695(boolean, android.content.Context, androidx.compose.ui.hapticfeedback.HapticFeedback, kotlinx.coroutines.CoroutineScope, com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository, com.tasbih.digitaltasbih.data.groups.CampaignItem, kotlin.jvm.functions.Function1, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit UmrahMegaRegisterModal$lambda$695$lambda$693$lambda$692(Context $context, HapticFeedback $haptic, CoroutineScope $coroutineScope, MutableState $generalErrorMsg$delegate, MutableState $name$delegate, MutableState $phone$delegate, MutableState $city$delegate, MutableState $agreeChecked$delegate, MutableState $consentChecked$delegate, MutableState $consentError$delegate, MutableState $isSubmitting$delegate, GroupCampaignRepository $repository, CampaignItem $campaign, Function1 $onSuccess) {
        if (!NetworkHelper.INSTANCE.isOnline($context)) {
            $haptic.performHapticFeedback-CdsT49E(HapticFeedbackType.Companion.getLongPress-5zf0vsI());
            $generalErrorMsg$delegate.setValue("📶 Kindly turn on the internet connection to register.");
            return Unit.INSTANCE;
        }
        if (StringsKt.isBlank(UmrahMegaRegisterModal$lambda$669($name$delegate))) {
            $haptic.performHapticFeedback-CdsT49E(HapticFeedbackType.Companion.getLongPress-5zf0vsI());
            $generalErrorMsg$delegate.setValue("Please enter your Full Name.");
            return Unit.INSTANCE;
        }
        if (!StringsKt.isBlank(UmrahMegaRegisterModal$lambda$672($phone$delegate)) && UmrahMegaRegisterModal$lambda$672($phone$delegate).length() >= 8) {
            if (StringsKt.isBlank(UmrahMegaRegisterModal$lambda$675($city$delegate))) {
                $haptic.performHapticFeedback-CdsT49E(HapticFeedbackType.Companion.getLongPress-5zf0vsI());
                $generalErrorMsg$delegate.setValue("Please enter your City / Location.");
                return Unit.INSTANCE;
            }
            if (!UmrahMegaRegisterModal$lambda$678($agreeChecked$delegate)) {
                $haptic.performHapticFeedback-CdsT49E(HapticFeedbackType.Companion.getLongPress-5zf0vsI());
                $generalErrorMsg$delegate.setValue("Please accept the Terms & Guidelines.");
                return Unit.INSTANCE;
            }
            if (!UmrahMegaRegisterModal$lambda$681($consentChecked$delegate)) {
                UmrahMegaRegisterModal$lambda$688($consentError$delegate, true);
                $haptic.performHapticFeedback-CdsT49E(HapticFeedbackType.Companion.getLongPress-5zf0vsI());
                $generalErrorMsg$delegate.setValue("⚠️ Please provide consent to proceed.");
                return Unit.INSTANCE;
            }
            UmrahMegaRegisterModal$lambda$685($isSubmitting$delegate, true);
            BuildersKt.launch$default($coroutineScope, (CoroutineContext) null, (CoroutineStart) null, new UmrahMegaRegisterModal.1.1.1.1($repository, $campaign, $name$delegate, $phone$delegate, $city$delegate, $onSuccess, $isSubmitting$delegate, (Continuation) null), 3, (Object) null);
            return Unit.INSTANCE;
        }
        $haptic.performHapticFeedback-CdsT49E(HapticFeedbackType.Companion.getLongPress-5zf0vsI());
        $generalErrorMsg$delegate.setValue("Please enter a valid WhatsApp / Mobile number.");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit UmrahMegaRegisterModal$lambda$695$lambda$694(boolean $isDarkTheme, MutableState $isSubmitting$delegate, RowScope $this$Button, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(499790196, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.UmrahMegaRegisterModal.<anonymous>.<anonymous> (LiveDhikrScreen.kt:4182)");
            }
            if (UmrahMegaRegisterModal$lambda$684($isSubmitting$delegate)) {
                $composer.startReplaceGroup(-1401846173);
                ComposerKt.sourceInformation($composer, "4183@187906L99");
                ProgressIndicatorKt.CircularProgressIndicator-LxG7B9w(SizeKt.size-3ABfNKs(Modifier.Companion, Dp.constructor-impl(18)), Color.Companion.getWhite-0d7_KjU(), Dp.constructor-impl(2), 0L, 0, $composer, 438, 24);
                $composer.endReplaceGroup();
            } else {
                $composer.startReplaceGroup(-1401696970);
                ComposerKt.sourceInformation($composer, "4185@188051L272");
                TextKt.Text--4IGK_g("JOIN & START RECITING ➔", (Modifier) null, $isDarkTheme ? ColorKt.Color(4280163086L) : Color.Companion.getWhite-0d7_KjU(), TextUnitKt.getSp(12.5d), (FontStyle) null, FontWeight.Companion.getExtraBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 199686, 0, 131026);
                $composer.endReplaceGroup();
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit UmrahMegaRegisterModal$lambda$697(Function0 $onDismiss, final boolean $isDarkTheme, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C4198@188522L107,4195@188405L224:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1327054242, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.UmrahMegaRegisterModal.<anonymous> (LiveDhikrScreen.kt:4195)");
            }
            ButtonKt.OutlinedButton($onDismiss, (Modifier) null, false, RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(12)), (ButtonColors) null, (ButtonElevation) null, (BorderStroke) null, (PaddingValues) null, (MutableInteractionSource) null, ComposableLambdaKt.rememberComposableLambda(-1496946960, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda23
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return LiveDhikrScreenKt.UmrahMegaRegisterModal$lambda$697$lambda$696($isDarkTheme, (RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer, 54), $composer, 805306368, 502);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit UmrahMegaRegisterModal$lambda$697$lambda$696(boolean $isDarkTheme, RowScope $this$OutlinedButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$OutlinedButton, "$this$OutlinedButton");
        ComposerKt.sourceInformation($composer, "C4199@188540L75:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1496946960, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.UmrahMegaRegisterModal.<anonymous>.<anonymous> (LiveDhikrScreen.kt:4199)");
            }
            TextKt.Text--4IGK_g("Cancel", (Modifier) null, $isDarkTheme ? Color.Companion.getWhite-0d7_KjU() : ColorKt.Color(4282865001L), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 6, 0, 131066);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    private static final void NoInternetConnectionDialog(final Function0<Unit> function0, final Function0<Unit> function02, Composer $composer, final int $changed) {
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(747974262);
        ComposerKt.sourceInformation($composer3, "C(NoInternetConnectionDialog)4234@189655L328,4243@190009L125,4213@188883L1338:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changedInstance(function0) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(function02) ? 32 : 16;
        }
        if (($dirty & 19) == 18 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(747974262, $dirty, -1, "com.tasbih.digitaltasbih.ui.screens.groups.NoInternetConnectionDialog (LiveDhikrScreen.kt:4212)");
            }
            $composer2 = $composer3;
            AndroidAlertDialog_androidKt.AlertDialog-Oix01E0(function0, ComposableLambdaKt.rememberComposableLambda(-1474278210, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda64
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.NoInternetConnectionDialog$lambda$728(function02, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), (Modifier) null, ComposableLambdaKt.rememberComposableLambda(422701248, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda65
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.NoInternetConnectionDialog$lambda$729(function0, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), (Function2) null, ComposableSingletons.LiveDhikrScreenKt.INSTANCE.getLambda$-1975286590$app(), ComposableSingletons.LiveDhikrScreenKt.INSTANCE.getLambda$-1026796861$app(), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(22)), Color.Companion.getWhite-0d7_KjU(), 0L, 0L, 0L, 0.0f, (DialogProperties) null, $composer2, ($dirty & 14) | 102435888, 0, 15892);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda66
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.NoInternetConnectionDialog$lambda$730(function0, function02, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit NoInternetConnectionDialog$lambda$728(Function0 $onRetry, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C4237@189752L48,4235@189669L304:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1474278210, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.NoInternetConnectionDialog.<anonymous> (LiveDhikrScreen.kt:4235)");
            }
            ButtonKt.Button($onRetry, (Modifier) null, false, RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(12)), ButtonDefaults.INSTANCE.buttonColors-ro_MJ88(ColorKt.Color(4278532908L), 0L, 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 6, 14), (ButtonElevation) null, (BorderStroke) null, (PaddingValues) null, (MutableInteractionSource) null, ComposableSingletons.LiveDhikrScreenKt.INSTANCE.getLambda$1918711982$app(), $composer, 805306368, 486);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit NoInternetConnectionDialog$lambda$729(Function0 $onDismiss, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C4244@190023L101:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(422701248, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.NoInternetConnectionDialog.<anonymous> (LiveDhikrScreen.kt:4244)");
            }
            ButtonKt.TextButton($onDismiss, (Modifier) null, false, (Shape) null, (ButtonColors) null, (ButtonElevation) null, (BorderStroke) null, (PaddingValues) null, (MutableInteractionSource) null, ComposableSingletons.LiveDhikrScreenKt.INSTANCE.getLambda$-990834115$app(), $composer, 805306368, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    private static final void SmartAutoCounterModal(final boolean isRunning, final long currentSpeedMs, final Function0<Unit> function0, final Function1<? super Long, Unit> function1, final Function0<Unit> function02, Composer $composer, final int $changed) {
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(2100165302);
        ComposerKt.sourceInformation($composer3, "C(SmartAutoCounterModal)P(1!1,4,3)4287@191866L416,4298@192308L77,4267@190656L1184,4262@190451L1940:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(isRunning) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changed(currentSpeedMs) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer3.changedInstance(function0) ? 256 : 128;
        }
        if (($changed & 3072) == 0) {
            $dirty |= $composer3.changedInstance(function1) ? 2048 : 1024;
        }
        if (($changed & 24576) == 0) {
            $dirty |= $composer3.changedInstance(function02) ? 16384 : 8192;
        }
        if (($dirty & 9363) == 9362 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(2100165302, $dirty, -1, "com.tasbih.digitaltasbih.ui.screens.groups.SmartAutoCounterModal (LiveDhikrScreen.kt:4261)");
            }
            $composer2 = $composer3;
            AndroidAlertDialog_androidKt.AlertDialog-Oix01E0(function02, ComposableLambdaKt.rememberComposableLambda(458823422, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda39
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.SmartAutoCounterModal$lambda$734(function0, function02, isRunning, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), (Modifier) null, ComposableLambdaKt.rememberComposableLambda(957380224, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda40
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.SmartAutoCounterModal$lambda$735(function02, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), (Function2) null, ComposableSingletons.LiveDhikrScreenKt.INSTANCE.getLambda$1455937026$app(), ComposableLambdaKt.rememberComposableLambda(1705215427, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda41
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.SmartAutoCounterModal$lambda$742(currentSpeedMs, function1, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), (Shape) null, 0L, 0L, 0L, 0L, 0.0f, (DialogProperties) null, $composer2, (($dirty >> 12) & 14) | 1772592, 0, 16276);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda42
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.SmartAutoCounterModal$lambda$743(isRunning, currentSpeedMs, function0, function1, function02, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01fa  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0206  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x023d  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0301  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0470  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0253  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x020c  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SmartAutoCounterModal$lambda$742(final long r75, kotlin.jvm.functions.Function1 r77, androidx.compose.runtime.Composer r78, int r79) {
        /*
            Method dump skipped, instructions count: 1142
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.SmartAutoCounterModal$lambda$742(long, kotlin.jvm.functions.Function1, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SmartAutoCounterModal$lambda$742$lambda$741$lambda$740$lambda$739$lambda$737$lambda$736(Function1 $onSelectSpeed, long $speed) {
        $onSelectSpeed.invoke(Long.valueOf($speed));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit SmartAutoCounterModal$lambda$742$lambda$741$lambda$740$lambda$739$lambda$738(String $label, long $currentSpeedMs, long $speed, RowScope $this$Button, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C4280@191524L84:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-581098106, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.SmartAutoCounterModal.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:4280)");
            }
            TextKt.Text--4IGK_g($label, (Modifier) null, $currentSpeedMs == $speed ? ColorKt.Color(4280163086L) : Color.Companion.getWhite-0d7_KjU(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 0, 0, 131066);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit SmartAutoCounterModal$lambda$734(final Function0 $onToggle, final Function0 $onDismiss, final boolean $isRunning, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C4289@191914L82,4293@192038L86,4294@192139L133,4288@191880L392:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(458823422, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.SmartAutoCounterModal.<anonymous> (LiveDhikrScreen.kt:4288)");
            }
            ComposerKt.sourceInformationMarkerStart($composer, -1452321328, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            boolean changed = $composer.changed($onToggle) | $composer.changed($onDismiss);
            Object rememberedValue = $composer.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda251
                    public final Object invoke() {
                        return LiveDhikrScreenKt.SmartAutoCounterModal$lambda$734$lambda$732$lambda$731($onToggle, $onDismiss);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            Function0 function0 = (Function0) obj;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button(function0, (Modifier) null, false, (Shape) null, ButtonDefaults.INSTANCE.buttonColors-ro_MJ88(ColorKt.Color($isRunning ? 4292617766L : 4278483031L), 0L, 0L, 0L, $composer, ButtonDefaults.$stable << 12, 14), (ButtonElevation) null, (BorderStroke) null, (PaddingValues) null, (MutableInteractionSource) null, ComposableLambdaKt.rememberComposableLambda(-2045974802, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda252
                public final Object invoke(Object obj2, Object obj3, Object obj4) {
                    return LiveDhikrScreenKt.SmartAutoCounterModal$lambda$734$lambda$733($isRunning, (RowScope) obj2, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, $composer, 54), $composer, 805306368, 494);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SmartAutoCounterModal$lambda$734$lambda$732$lambda$731(Function0 $onToggle, Function0 $onDismiss) {
        $onToggle.invoke();
        $onDismiss.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit SmartAutoCounterModal$lambda$734$lambda$733(boolean $isRunning, RowScope $this$Button, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C4295@192157L101:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-2045974802, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.SmartAutoCounterModal.<anonymous>.<anonymous> (LiveDhikrScreen.kt:4295)");
            }
            TextKt.Text--4IGK_g($isRunning ? "STOP AUTO" : "START AUTO", (Modifier) null, Color.Companion.getWhite-0d7_KjU(), 0L, (FontStyle) null, FontWeight.Companion.getBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 196992, 0, 131034);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit SmartAutoCounterModal$lambda$735(Function0 $onDismiss, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C4299@192322L53:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(957380224, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.SmartAutoCounterModal.<anonymous> (LiveDhikrScreen.kt:4299)");
            }
            ButtonKt.OutlinedButton($onDismiss, (Modifier) null, false, (Shape) null, (ButtonColors) null, (ButtonElevation) null, (BorderStroke) null, (PaddingValues) null, (MutableInteractionSource) null, ComposableSingletons.LiveDhikrScreenKt.INSTANCE.getLambda$-1748488974$app(), $composer, 805306368, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    private static final void JoinByCodeDialog(final Function0<Unit> function0, final Function1<? super String, Unit> function1, Composer $composer, final int $changed) {
        Object obj;
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(1858842629);
        ComposerKt.sourceInformation($composer3, "C(JoinByCodeDialog)4310@192542L31,4327@193313L366,4336@193705L58,4314@192760L527,4311@192578L1191:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changedInstance(function0) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(function1) ? 32 : 16;
        }
        if (($dirty & 19) == 18 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1858842629, $dirty, -1, "com.tasbih.digitaltasbih.ui.screens.groups.JoinByCodeDialog (LiveDhikrScreen.kt:4309)");
            }
            ComposerKt.sourceInformationMarkerStart($composer3, -358624860, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue = $composer3.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = SnapshotStateKt.mutableStateOf$default("", (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer3.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            final MutableState code$delegate = (MutableState) obj;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            $composer2 = $composer3;
            AndroidAlertDialog_androidKt.AlertDialog-Oix01E0(function0, ComposableLambdaKt.rememberComposableLambda(984778829, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda12
                public final Object invoke(Object obj2, Object obj3) {
                    return LiveDhikrScreenKt.JoinByCodeDialog$lambda$749(function1, code$delegate, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer3, 54), (Modifier) null, ComposableLambdaKt.rememberComposableLambda(-1911234865, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda13
                public final Object invoke(Object obj2, Object obj3) {
                    return LiveDhikrScreenKt.JoinByCodeDialog$lambda$750(function0, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer3, 54), (Function2) null, ComposableSingletons.LiveDhikrScreenKt.INSTANCE.getLambda$-512281263$app(), ComposableLambdaKt.rememberComposableLambda(-1960288110, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda14
                public final Object invoke(Object obj2, Object obj3) {
                    return LiveDhikrScreenKt.JoinByCodeDialog$lambda$754(code$delegate, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer3, 54), (Shape) null, 0L, 0L, 0L, 0L, 0.0f, (DialogProperties) null, $composer2, ($dirty & 14) | 1772592, 0, 16276);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda15
                public final Object invoke(Object obj2, Object obj3) {
                    return LiveDhikrScreenKt.JoinByCodeDialog$lambda$755(function0, function1, $changed, (Composer) obj2, ((Integer) obj3).intValue());
                }
            });
        }
    }

    private static final String JoinByCodeDialog$lambda$745(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01ae  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0229  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x01c0  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit JoinByCodeDialog$lambda$754(final androidx.compose.runtime.MutableState r54, androidx.compose.runtime.Composer r55, int r56) {
        /*
            Method dump skipped, instructions count: 559
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.JoinByCodeDialog$lambda$754(androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit JoinByCodeDialog$lambda$754$lambda$753$lambda$752$lambda$751(MutableState $code$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String upperCase = it.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        $code$delegate.setValue(StringsKt.take(StringsKt.trim(upperCase).toString(), 8));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit JoinByCodeDialog$lambda$749(final Function1 $onJoin, MutableState $code$delegate, Composer $composer, int $changed) {
        Object obj;
        final MutableState mutableState;
        ComposerKt.sourceInformation($composer, "C4329@193361L52,4331@193506L41,4328@193327L342:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(984778829, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.JoinByCodeDialog.<anonymous> (LiveDhikrScreen.kt:4328)");
            }
            ComposerKt.sourceInformationMarkerStart($composer, 1479651137, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            boolean changed = $composer.changed($onJoin);
            Object rememberedValue = $composer.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                mutableState = $code$delegate;
                obj = new Function0() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda22
                    public final Object invoke() {
                        return LiveDhikrScreenKt.JoinByCodeDialog$lambda$749$lambda$748$lambda$747($onJoin, mutableState);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                mutableState = $code$delegate;
                obj = rememberedValue;
            }
            Function0 function0 = (Function0) obj;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button(function0, (Modifier) null, StringsKt.trim(JoinByCodeDialog$lambda$745(mutableState)).toString().length() >= 4, (Shape) null, ButtonDefaults.INSTANCE.buttonColors-ro_MJ88(BrightGold, 0L, 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 6, 14), (ButtonElevation) null, (BorderStroke) null, (PaddingValues) null, (MutableInteractionSource) null, ComposableSingletons.LiveDhikrScreenKt.INSTANCE.getLambda$-1040670659$app(), $composer, 805306368, 490);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit JoinByCodeDialog$lambda$749$lambda$748$lambda$747(Function1 $onJoin, MutableState $code$delegate) {
        if (StringsKt.trim(JoinByCodeDialog$lambda$745($code$delegate)).toString().length() >= 4) {
            $onJoin.invoke(StringsKt.trim(JoinByCodeDialog$lambda$745($code$delegate)).toString());
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit JoinByCodeDialog$lambda$750(Function0 $onDismiss, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C4336@193707L54:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1911234865, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.JoinByCodeDialog.<anonymous> (LiveDhikrScreen.kt:4336)");
            }
            ButtonKt.OutlinedButton($onDismiss, (Modifier) null, false, (Shape) null, (ButtonColors) null, (ButtonElevation) null, (BorderStroke) null, (PaddingValues) null, (MutableInteractionSource) null, ComposableSingletons.LiveDhikrScreenKt.INSTANCE.getLambda$894063681$app(), $composer, 805306368, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    private static final void DhikrPickerSheet(final Function0<Unit> function0, final Function1<? super DhikrCatalogItem, Unit> function1, boolean isDarkTheme, Composer $composer, final int $changed, final int i) {
        final Function0<Unit> function02;
        final Function1<? super DhikrCatalogItem, Unit> function12;
        boolean z;
        Object obj;
        MutableState searchQuery$delegate;
        Object obj2;
        Composer $composer2;
        final boolean isDarkTheme2;
        Composer $composer3 = $composer.startRestartGroup(-981706224);
        ComposerKt.sourceInformation($composer3, "C(DhikrPickerSheet)P(1,2)4399@199796L59,4400@199879L31,4401@199934L34,4408@200209L7525,4403@199974L7760:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            function02 = function0;
            $dirty |= $composer3.changedInstance(function02) ? 4 : 2;
        } else {
            function02 = function0;
        }
        if (($changed & 48) == 0) {
            function12 = function1;
            $dirty |= $composer3.changedInstance(function12) ? 32 : 16;
        } else {
            function12 = function1;
        }
        int i2 = i & 4;
        if (i2 != 0) {
            $dirty |= 384;
            z = isDarkTheme;
        } else if (($changed & 384) == 0) {
            z = isDarkTheme;
            $dirty |= $composer3.changed(z) ? 256 : 128;
        } else {
            z = isDarkTheme;
        }
        if (($dirty & 147) == 146 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
            isDarkTheme2 = z;
        } else {
            final boolean isDarkTheme3 = i2 != 0 ? false : z;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-981706224, $dirty, -1, "com.tasbih.digitaltasbih.ui.screens.groups.DhikrPickerSheet (LiveDhikrScreen.kt:4398)");
            }
            SheetState sheetState = ModalBottomSheetKt.rememberModalBottomSheetState(true, (Function1) null, $composer3, 6, 2);
            ComposerKt.sourceInformationMarkerStart($composer3, 1473133231, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue = $composer3.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = SnapshotStateKt.mutableStateOf$default("", (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer3.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            MutableState searchQuery$delegate2 = (MutableState) obj;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 1473134994, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue2 = $composer3.rememberedValue();
            if (rememberedValue2 == Composer.Companion.getEmpty()) {
                searchQuery$delegate = searchQuery$delegate2;
                obj2 = SnapshotStateKt.mutableStateOf$default("all", (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer3.updateRememberedValue(obj2);
            } else {
                searchQuery$delegate = searchQuery$delegate2;
                obj2 = rememberedValue2;
            }
            final MutableState selectedCat$delegate = (MutableState) obj2;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final MutableState searchQuery$delegate3 = searchQuery$delegate;
            boolean isDarkTheme4 = isDarkTheme3;
            $composer2 = $composer3;
            ModalBottomSheetKt.ModalBottomSheet-dYc4hso(function0, (Modifier) null, sheetState, 0.0f, RoundedCornerShapeKt.RoundedCornerShape-a9UjIt4$default(Dp.constructor-impl(26), Dp.constructor-impl(26), 0.0f, 0.0f, 12, (Object) null), isDarkTheme3 ? DarkCardBg : Color.Companion.getWhite-0d7_KjU(), 0L, 0.0f, 0L, (Function2) null, (Function2) null, (ModalBottomSheetProperties) null, ComposableLambdaKt.rememberComposableLambda(1830341779, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda26
                public final Object invoke(Object obj3, Object obj4, Object obj5) {
                    return LiveDhikrScreenKt.DhikrPickerSheet$lambda$781(isDarkTheme3, function12, function02, searchQuery$delegate3, selectedCat$delegate, (ColumnScope) obj3, (Composer) obj4, ((Integer) obj5).intValue());
                }
            }, $composer3, 54), $composer2, $dirty & 14, 384, 4042);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            isDarkTheme2 = isDarkTheme4;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda27
                public final Object invoke(Object obj3, Object obj4) {
                    return LiveDhikrScreenKt.DhikrPickerSheet$lambda$782(function0, function1, isDarkTheme2, $changed, i, (Composer) obj3, ((Integer) obj4).intValue());
                }
            });
        }
    }

    private static final String DhikrPickerSheet$lambda$757(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String DhikrPickerSheet$lambda$760(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01ea  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0295  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x034c  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x03dc  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x047d  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x04e5  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x056c  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x056f A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x05cb  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0630  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x03ee  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0350  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0298  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01f0  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit DhikrPickerSheet$lambda$781(final boolean r129, final kotlin.jvm.functions.Function1 r130, kotlin.jvm.functions.Function0 r131, final androidx.compose.runtime.MutableState r132, final androidx.compose.runtime.MutableState r133, androidx.compose.foundation.layout.ColumnScope r134, androidx.compose.runtime.Composer r135, int r136) {
        /*
            Method dump skipped, instructions count: 1590
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.DhikrPickerSheet$lambda$781(boolean, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function0, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit DhikrPickerSheet$lambda$781$lambda$780$lambda$763$lambda$762(boolean $isDarkTheme, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C4427@200940L223:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(651364554, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.DhikrPickerSheet.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:4427)");
            }
            IconKt.Icon-ww6aTOc(CloseKt.getClose(Icons.INSTANCE.getDefault()), "Close", (Modifier) null, $isDarkTheme ? Color.Companion.getWhite-0d7_KjU() : ColorKt.Color(4278532908L), $composer, 48, 4);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit DhikrPickerSheet$lambda$781$lambda$780$lambda$765$lambda$764(MutableState $searchQuery$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $searchQuery$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit DhikrPickerSheet$lambda$781$lambda$780$lambda$770$lambda$769(boolean $isDarkTheme, MutableState $selectedCat$delegate, LazyListScope $this$LazyRow) {
        Intrinsics.checkNotNullParameter($this$LazyRow, "$this$LazyRow");
        List cats = CollectionsKt.listOf(new Pair[]{TuplesKt.to("all", "All"), TuplesKt.to("tasbih", "Core Dhikrs"), TuplesKt.to("istighfar", "Istighfar"), TuplesKt.to("durood", "Durood Shareef"), TuplesKt.to("asma", "99 Names of Allah")});
        $this$LazyRow.items(cats.size(), (Function1) null, new LiveDhikrScreenKt$DhikrPickerSheet$lambda$781$lambda$780$lambda$770$lambda$769$.inlined.items.default.3(LiveDhikrScreenKt$DhikrPickerSheet$lambda$781$lambda$780$lambda$770$lambda$769$.inlined.items.default.1.INSTANCE, cats), ComposableLambdaKt.composableLambdaInstance(-632812321, true, new LiveDhikrScreenKt$DhikrPickerSheet$lambda$781$lambda$780$lambda$770$lambda$769$.inlined.items.default.4(cats, $isDarkTheme, $selectedCat$delegate)));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit DhikrPickerSheet$lambda$781$lambda$780$lambda$779$lambda$778(List $filtered, boolean $isDarkTheme, Function1 $onSelect, LazyListScope $this$LazyColumn) {
        Intrinsics.checkNotNullParameter($this$LazyColumn, "$this$LazyColumn");
        $this$LazyColumn.items($filtered.size(), (Function1) null, new LiveDhikrScreenKt$DhikrPickerSheet$lambda$781$lambda$780$lambda$779$lambda$778$.inlined.items.default.3(LiveDhikrScreenKt$DhikrPickerSheet$lambda$781$lambda$780$lambda$779$lambda$778$.inlined.items.default.1.INSTANCE, $filtered), ComposableLambdaKt.composableLambdaInstance(-632812321, true, new LiveDhikrScreenKt$DhikrPickerSheet$lambda$781$lambda$780$lambda$779$lambda$778$.inlined.items.default.4($filtered, $isDarkTheme, $onSelect)));
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code restructure failed: missing block: B:385:0x0343, code lost:
    
        if (r1 > 0.0f) goto L143;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0450  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0483  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x04b0  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x04e2  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0513  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0549  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0567  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x057a  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0587  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0595  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x05a9  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x05c3  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0618  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x069e  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x06aa  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x06dd  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x07d3  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x07df  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0818  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x096a  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0976  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x09af  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0a88  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0a94  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0ac7  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0c70  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0c7c  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0cb5  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0de7  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0df3  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0e2c  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0f22  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0ff6  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x1002  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x1086  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x1199  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x12a6  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x1324  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x1330  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x1369  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x1484  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x1491  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x1559  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x1565  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x159e  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x167e  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x168a  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x16c1  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x1826  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x18b5  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x1a2f  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x1a01  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x189f  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x16d7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:316:0x1690  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x15b4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:319:0x156b  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x1486  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x137f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:324:0x1336  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x12b8  */
    /* JADX WARN: Removed duplicated region for block: B:326:0x11ab  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x1098  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0ff8  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x0f2e  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x0e42 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:333:0x0df9  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x0ccb  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x0c82  */
    /* JADX WARN: Removed duplicated region for block: B:338:0x0add  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x0a98  */
    /* JADX WARN: Removed duplicated region for block: B:341:0x09c5  */
    /* JADX WARN: Removed duplicated region for block: B:342:0x097c  */
    /* JADX WARN: Removed duplicated region for block: B:344:0x082e  */
    /* JADX WARN: Removed duplicated region for block: B:345:0x07e5  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x06f3  */
    /* JADX WARN: Removed duplicated region for block: B:348:0x06ae  */
    /* JADX WARN: Removed duplicated region for block: B:349:0x062a  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x05c5  */
    /* JADX WARN: Removed duplicated region for block: B:353:0x05ac  */
    /* JADX WARN: Removed duplicated region for block: B:354:0x059c  */
    /* JADX WARN: Removed duplicated region for block: B:355:0x058a  */
    /* JADX WARN: Removed duplicated region for block: B:356:0x057d  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x056d  */
    /* JADX WARN: Removed duplicated region for block: B:358:0x055a  */
    /* JADX WARN: Removed duplicated region for block: B:359:0x0527  */
    /* JADX WARN: Removed duplicated region for block: B:360:0x04f4  */
    /* JADX WARN: Removed duplicated region for block: B:361:0x04c2  */
    /* JADX WARN: Removed duplicated region for block: B:362:0x0490  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x0461  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x0430  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x0403  */
    /* JADX WARN: Removed duplicated region for block: B:366:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:397:0x027e  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x0247  */
    /* JADX WARN: Removed duplicated region for block: B:399:0x0210  */
    /* JADX WARN: Removed duplicated region for block: B:400:0x01d8  */
    /* JADX WARN: Removed duplicated region for block: B:401:0x01a5  */
    /* JADX WARN: Removed duplicated region for block: B:402:0x0170  */
    /* JADX WARN: Removed duplicated region for block: B:403:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0165  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0196  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x026d  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x03c9  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x03f6  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0423  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static final void CreateGroupZikrScreen(com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository r185, java.lang.String r186, final kotlin.jvm.functions.Function0<kotlin.Unit> r187, final kotlin.jvm.functions.Function7<? super java.lang.String, ? super java.lang.String, ? super java.lang.Long, ? super java.lang.Boolean, ? super java.lang.String, ? super java.lang.Boolean, ? super java.lang.String, kotlin.Unit> r188, final kotlin.jvm.functions.Function0<kotlin.Unit> r189, boolean r190, final kotlin.jvm.functions.Function0<kotlin.Unit> r191, androidx.compose.runtime.Composer r192, final int r193, final int r194) {
        /*
            Method dump skipped, instructions count: 6762
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.CreateGroupZikrScreen(com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository, java.lang.String, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function7, kotlin.jvm.functions.Function0, boolean, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean CreateGroupZikrScreen$lambda$785(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void CreateGroupZikrScreen$lambda$786(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int CreateGroupZikrScreen$lambda$788(MutableIntState $createTourStep$delegate) {
        return ((IntState) $createTourStep$delegate).getIntValue();
    }

    private static final LayoutCoordinates CreateGroupZikrScreen$lambda$791(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates CreateGroupZikrScreen$lambda$794(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates CreateGroupZikrScreen$lambda$797(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates CreateGroupZikrScreen$lambda$800(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final LayoutCoordinates CreateGroupZikrScreen$lambda$803(MutableState<LayoutCoordinates> mutableState) {
        return (LayoutCoordinates) ((State) mutableState).getValue();
    }

    private static final String CreateGroupZikrScreen$lambda$808(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final String CreateGroupZikrScreen$lambda$811(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final String CreateGroupZikrScreen$lambda$814(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final String CreateGroupZikrScreen$lambda$817(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final String CreateGroupZikrScreen$lambda$820(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final boolean CreateGroupZikrScreen$lambda$823(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void CreateGroupZikrScreen$lambda$824(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean CreateGroupZikrScreen$lambda$829(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void CreateGroupZikrScreen$lambda$830(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String CreateGroupZikrScreen$lambda$832(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$835$lambda$834(Function0 $onDismiss) {
        $onDismiss.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$837$lambda$836(MutableState $createRootCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $createRootCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$845$lambda$844$lambda$841$lambda$840(MutableIntState $createTourStep$delegate, MutableState $isCreateTourOpen$delegate) {
        $createTourStep$delegate.setIntValue(1);
        CreateGroupZikrScreen$lambda$786($isCreateTourOpen$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0161  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01ab  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0166  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$845$lambda$844$lambda$843(boolean r32, androidx.compose.runtime.Composer r33, int r34) {
        /*
            Method dump skipped, instructions count: 433
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.CreateGroupZikrScreen$lambda$917$lambda$906$lambda$845$lambda$844$lambda$843(boolean, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$850$lambda$849(boolean $isDarkTheme, MutableState $selectedLang$delegate, LazyListScope $this$LazyRow) {
        Intrinsics.checkNotNullParameter($this$LazyRow, "$this$LazyRow");
        List langs = CollectionsKt.listOf(new Pair[]{TuplesKt.to("en", "English"), TuplesKt.to("hi", "हिंदी"), TuplesKt.to("ur", "اردو"), TuplesKt.to("ar", "العربية"), TuplesKt.to("bn", "বাংলা"), TuplesKt.to("gu", "ગુજરાતી")});
        $this$LazyRow.items(langs.size(), (Function1) null, new LiveDhikrScreenKt$CreateGroupZikrScreen$lambda$917$lambda$906$lambda$850$lambda$849$.inlined.items.default.3(LiveDhikrScreenKt$CreateGroupZikrScreen$lambda$917$lambda$906$lambda$850$lambda$849$.inlined.items.default.1.INSTANCE, langs), ComposableLambdaKt.composableLambdaInstance(-632812321, true, new LiveDhikrScreenKt$CreateGroupZikrScreen$lambda$917$lambda$906$lambda$850$lambda$849$.inlined.items.default.4(langs, $isDarkTheme, $selectedLang$delegate)));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$852$lambda$851(MutableState $chooseZikrCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $chooseZikrCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0b0e  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0c3f  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0c4b  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0c84  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0cf5  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0f04  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x1062  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x106e  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x10a7  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x1118  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x12e6  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x12f2  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x1329  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x18fd  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x133f  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x12f8  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x14f7  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x1503  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x153c  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x15ab  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x16bd  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x18a0  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x1654  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x1552 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:203:0x1509  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x10bd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:206:0x1074  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0f16  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0cfa  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0c9a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0c51  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0b24 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:214:0x0adb  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x09db A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0992  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x08b7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:220:0x086e  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x0790 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:223:0x0747  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x06a7  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0513 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x039a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0351  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01ce  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x033f  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x034b  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0384  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x04b8  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x04c4  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x04fd  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0695  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0735  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0741  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x077a  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x085c  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0868  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x08a1  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0980  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x098c  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x09c5  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0ac9  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0ad5  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$871(long r163, long r165, final androidx.compose.runtime.MutableState r167, final androidx.compose.runtime.MutableState r168, androidx.compose.runtime.MutableState r169, androidx.compose.runtime.MutableState r170, androidx.compose.foundation.layout.ColumnScope r171, androidx.compose.runtime.Composer r172, int r173) {
        /*
            Method dump skipped, instructions count: 6403
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.CreateGroupZikrScreen$lambda$917$lambda$906$lambda$871(long, long, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$871$lambda$870$lambda$857$lambda$856(MutableState $showPicker$delegate) {
        CreateGroupZikrScreen$lambda$830($showPicker$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$871$lambda$870$lambda$864$lambda$863(MutableState $title$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $title$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$873$lambda$872(MutableState $targetCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $targetCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0b47  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0b4e  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0cdf  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0dbb  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0e44  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0e50  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0e89  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0f1c  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0f3c  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0f56  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0f99  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x1034  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x1040  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x1079  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x10ea  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x116f  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x118f  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x11a5  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x11e1  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x1269  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x1275  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x12ac  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x1318  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x13a7  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x131f  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x12c0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:204:0x127b  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x11ed  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x11af  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x1197  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x1177  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x10f1  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x108f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:212:0x1046  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0fab  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x0f60  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0f46  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0f24  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0e9f  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0e56  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x0dc9  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0cf1  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x0850 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x0807  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x070e  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x051b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x04d2  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x03a0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0357  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01d4  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0345  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0351  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x038a  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x04cc  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0505  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x06fe  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x07f5  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0801  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x083a  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x08dd  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$897(long r139, long r141, androidx.compose.runtime.MutableState r143, final androidx.compose.runtime.MutableState r144, final androidx.compose.runtime.MutableState r145, androidx.compose.runtime.MutableState r146, androidx.compose.foundation.layout.ColumnScope r147, androidx.compose.runtime.Composer r148, int r149) {
        /*
            Method dump skipped, instructions count: 5060
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.CreateGroupZikrScreen$lambda$917$lambda$906$lambda$897(long, long, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$897$lambda$896$lambda$879$lambda$878(MutableState $targetStr$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String str = it;
        Appendable sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (Character.isDigit(charAt)) {
                sb.append(charAt);
            }
        }
        $targetStr$delegate.setValue(((StringBuilder) sb).toString());
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$897$lambda$896$lambda$884$lambda$883$lambda$881$lambda$880(String $preset, MutableState $targetStr$delegate) {
        if (Intrinsics.areEqual($preset, "+500")) {
            Long longOrNull = StringsKt.toLongOrNull(CreateGroupZikrScreen$lambda$817($targetStr$delegate));
            long cur = longOrNull != null ? longOrNull.longValue() : 0L;
            $targetStr$delegate.setValue(String.valueOf(500 + cur));
        } else {
            $targetStr$delegate.setValue(StringsKt.replace$default($preset, ",", "", false, 4, (Object) null));
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$897$lambda$896$lambda$886$lambda$885(MutableState $note$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $note$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$897$lambda$896$lambda$888$lambda$887(MutableState $privacyCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $privacyCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$897$lambda$896$lambda$895$lambda$890$lambda$889(MutableState $isPrivate$delegate) {
        CreateGroupZikrScreen$lambda$824($isPrivate$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$897$lambda$896$lambda$895$lambda$893$lambda$892(MutableState $isPrivate$delegate) {
        CreateGroupZikrScreen$lambda$824($isPrivate$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$899$lambda$898(MutableState $createBtnCoords$delegate, LayoutCoordinates it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $createBtnCoords$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$906$lambda$905$lambda$902$lambda$901(Function7 $onCreate, MutableState $targetStr$delegate, MutableState $title$delegate, MutableState $arabic$delegate, MutableState $isPrivate$delegate, MutableState $note$delegate) {
        Long longOrNull = StringsKt.toLongOrNull(CreateGroupZikrScreen$lambda$817($targetStr$delegate));
        long tgt = longOrNull != null ? longOrNull.longValue() : 1000L;
        String CreateGroupZikrScreen$lambda$808 = CreateGroupZikrScreen$lambda$808($title$delegate);
        if (StringsKt.isBlank(CreateGroupZikrScreen$lambda$808)) {
            CreateGroupZikrScreen$lambda$808 = "Group Dhikr";
        }
        String finalTitle = CreateGroupZikrScreen$lambda$808;
        $onCreate.invoke(finalTitle, CreateGroupZikrScreen$lambda$811($arabic$delegate), Long.valueOf(tgt), Boolean.valueOf(CreateGroupZikrScreen$lambda$823($isPrivate$delegate)), "", false, CreateGroupZikrScreen$lambda$820($note$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$908$lambda$907(MutableState $showPicker$delegate) {
        CreateGroupZikrScreen$lambda$830($showPicker$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$910$lambda$909(MutableState $title$delegate, MutableState $arabic$delegate, MutableState $meaning$delegate, MutableState $showPicker$delegate, DhikrCatalogItem item) {
        Intrinsics.checkNotNullParameter(item, "item");
        $title$delegate.setValue(item.getTranslit());
        $arabic$delegate.setValue(item.getArabic());
        $meaning$delegate.setValue(item.getMeaning());
        CreateGroupZikrScreen$lambda$830($showPicker$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$912$lambda$911(GroupCampaignRepository $repository, MutableIntState $createTourStep$delegate, MutableState $isCreateTourOpen$delegate) {
        if (CreateGroupZikrScreen$lambda$788($createTourStep$delegate) >= 4) {
            $repository.setCreateTourDone(true);
            CreateGroupZikrScreen$lambda$786($isCreateTourOpen$delegate, false);
        } else {
            $createTourStep$delegate.setIntValue(CreateGroupZikrScreen$lambda$788($createTourStep$delegate) + 1);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$914$lambda$913(GroupCampaignRepository $repository, MutableState $isCreateTourOpen$delegate) {
        $repository.setCreateTourDone(true);
        CreateGroupZikrScreen$lambda$786($isCreateTourOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit CreateGroupZikrScreen$lambda$917$lambda$916$lambda$915(GroupCampaignRepository $repository, MutableState $isCreateTourOpen$delegate) {
        $repository.setCreateTourDone(true);
        CreateGroupZikrScreen$lambda$786($isCreateTourOpen$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x04f5  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x04e6  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0460  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x03d8  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x035b  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x03eb  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0471  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void WelcomeToGroupCampaignsDialog(@org.jetbrains.annotations.NotNull final com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository r26, @org.jetbrains.annotations.NotNull final kotlin.jvm.functions.Function0<kotlin.Unit> r27, @org.jetbrains.annotations.Nullable androidx.compose.runtime.Composer r28, final int r29) {
        /*
            Method dump skipped, instructions count: 1292
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.WelcomeToGroupCampaignsDialog(com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int):void");
    }

    private static final String WelcomeToGroupCampaignsDialog$lambda$920(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final String WelcomeToGroupCampaignsDialog$lambda$923(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final String WelcomeToGroupCampaignsDialog$lambda$927(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final String WelcomeToGroupCampaignsDialog$lambda$930(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final String WelcomeToGroupCampaignsDialog$lambda$933(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final boolean WelcomeToGroupCampaignsDialog$lambda$936(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void WelcomeToGroupCampaignsDialog$lambda$937(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean WelcomeToGroupCampaignsDialog$lambda$939(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void WelcomeToGroupCampaignsDialog$lambda$940(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean WelcomeToGroupCampaignsDialog$lambda$942(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void WelcomeToGroupCampaignsDialog$lambda$943(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean WelcomeToGroupCampaignsDialog$lambda$945(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void WelcomeToGroupCampaignsDialog$lambda$946(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998(final GroupCampaignRepository $repository, final Function0 $onDismiss, final MutableState $firstName$delegate, final MutableState $lastName$delegate, final MutableState $showCountryMenu$delegate, final MutableState $country$delegate, final List $statesList, final MutableState $state$delegate, final MutableState $showStateMenu$delegate, final List $citiesList, final MutableState $city$delegate, final MutableState $showCityMenu$delegate, final MutableState $consentChecked$delegate, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C5386@242947L40,5387@243026L38,5388@243075L20379,5381@242752L20702:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-18292397, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.WelcomeToGroupCampaignsDialog.<anonymous> (LiveDhikrScreen.kt:5381)");
            }
            CardKt.Card(PaddingKt.padding-VpY3zN4$default(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, (Object) null), 0.0f, Dp.constructor-impl(16), 1, (Object) null), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(22)), CardDefaults.INSTANCE.cardColors-ro_MJ88(Color.Companion.getWhite-0d7_KjU(), 0L, 0L, 0L, $composer, (CardDefaults.$stable << 12) | 6, 14), CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl(8), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, $composer, (CardDefaults.$stable << 18) | 6, 62), (BorderStroke) null, ComposableLambdaKt.rememberComposableLambda(1593479493, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda87
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return LiveDhikrScreenKt.WelcomeToGroupCampaignsDialog$lambda$998$lambda$997($repository, $onDismiss, $firstName$delegate, $lastName$delegate, $showCountryMenu$delegate, $country$delegate, $statesList, $state$delegate, $showStateMenu$delegate, $citiesList, $city$delegate, $showCityMenu$delegate, $consentChecked$delegate, (ColumnScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer, 54), $composer, 196614, 16);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0c4e  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0c5a  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0c93  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0d8a  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0e88  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0e94  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0ecd  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x1056  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x1062  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x109b  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x118e  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x129d  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x12a9  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x12e2  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x143a  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x1446  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x147f  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x15b5  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x1651  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x165d  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x1696  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x177e  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x178a  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x17c3  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x1834  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x184e  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x197b  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x1987  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x19c0  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x1b4c  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x1b58  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x1b91  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x1c73  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x1c7f  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x1cb8  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x1dac  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x1e10  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x1f3d  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x1f49  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x1f82  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x210b  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x2117  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x2150  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x2236  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01fa  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x2242  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x227b  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x2372  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x23d4  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x254e  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x255a  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x2593  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x2673  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0206  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x267f  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x26b8  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x2774  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x282c  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x2838  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x286f  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x29b5  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x2a68  */
    /* JADX WARN: Removed duplicated region for block: B:322:0x2885  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x283e  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x2786  */
    /* JADX WARN: Removed duplicated region for block: B:326:0x26ce A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:327:0x2685  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x25a9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:330:0x2560  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x2443  */
    /* JADX WARN: Removed duplicated region for block: B:333:0x2380  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x2291 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:336:0x2248  */
    /* JADX WARN: Removed duplicated region for block: B:338:0x2166 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:339:0x211d  */
    /* JADX WARN: Removed duplicated region for block: B:341:0x1f96 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:342:0x1f4f  */
    /* JADX WARN: Removed duplicated region for block: B:344:0x1e7b  */
    /* JADX WARN: Removed duplicated region for block: B:345:0x1dba  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x1cce A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:348:0x1c85  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x1ba7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:351:0x1b5e  */
    /* JADX WARN: Removed duplicated region for block: B:353:0x19d6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:354:0x198d  */
    /* JADX WARN: Removed duplicated region for block: B:355:0x1854  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x17d9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:358:0x1790  */
    /* JADX WARN: Removed duplicated region for block: B:360:0x16ac A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:361:0x1663  */
    /* JADX WARN: Removed duplicated region for block: B:362:0x15c7  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x1495 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:365:0x144c  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x12f8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:368:0x12af  */
    /* JADX WARN: Removed duplicated region for block: B:369:0x119c  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0324  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x10b1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:372:0x1068  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x0ee3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:375:0x0e9a  */
    /* JADX WARN: Removed duplicated region for block: B:376:0x0d98  */
    /* JADX WARN: Removed duplicated region for block: B:378:0x0ca9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:379:0x0c60  */
    /* JADX WARN: Removed duplicated region for block: B:381:0x0aa9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:382:0x0a60  */
    /* JADX WARN: Removed duplicated region for block: B:384:0x098a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:385:0x0941  */
    /* JADX WARN: Removed duplicated region for block: B:387:0x0860 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:388:0x0817  */
    /* JADX WARN: Removed duplicated region for block: B:390:0x06d2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:391:0x0689  */
    /* JADX WARN: Removed duplicated region for block: B:393:0x04e2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:394:0x0499  */
    /* JADX WARN: Removed duplicated region for block: B:396:0x037f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:397:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0330  */
    /* JADX WARN: Removed duplicated region for block: B:400:0x020c  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0369  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0487  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0493  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x04cc  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0677  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0683  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x06bc  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0805  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0811  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x084a  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x092f  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x093b  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0974  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0a4e  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0a5a  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0a93  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997(final com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository r168, final kotlin.jvm.functions.Function0 r169, final androidx.compose.runtime.MutableState r170, final androidx.compose.runtime.MutableState r171, final androidx.compose.runtime.MutableState r172, final androidx.compose.runtime.MutableState r173, java.util.List r174, final androidx.compose.runtime.MutableState r175, final androidx.compose.runtime.MutableState r176, java.util.List r177, final androidx.compose.runtime.MutableState r178, final androidx.compose.runtime.MutableState r179, final androidx.compose.runtime.MutableState r180, androidx.compose.foundation.layout.ColumnScope r181, androidx.compose.runtime.Composer r182, int r183) {
        /*
            Method dump skipped, instructions count: 10862
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.WelcomeToGroupCampaignsDialog$lambda$998$lambda$997(com.tasbih.digitaltasbih.data.groups.GroupCampaignRepository, kotlin.jvm.functions.Function0, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, java.util.List, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, java.util.List, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$965$lambda$959$lambda$958$lambda$956$lambda$955(MutableState $firstName$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $firstName$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Composable
    @ComposableInferredTarget(scheme = "[androidx.compose.ui.UiComposable[androidx.compose.ui.UiComposable]]")
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$965$lambda$959$lambda$958$lambda$957(MutableState $firstName$delegate, Function2 innerTextField, Composer $composer, int $changed) {
        int $dirty;
        Composer composer = $composer;
        Intrinsics.checkNotNullParameter(innerTextField, "innerTextField");
        ComposerKt.sourceInformation(composer, "C5493@248114L16:LiveDhikrScreen.kt#50dkb0");
        int $dirty2 = $changed;
        if (($changed & 6) == 0) {
            $dirty2 |= composer.changedInstance(innerTextField) ? 4 : 2;
        }
        if (($dirty2 & 19) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(27367567, $dirty2, -1, "com.tasbih.digitaltasbih.ui.screens.groups.WelcomeToGroupCampaignsDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:5486)");
            }
            if (WelcomeToGroupCampaignsDialog$lambda$920($firstName$delegate).length() == 0) {
                composer.startReplaceGroup(-736507345);
                ComposerKt.sourceInformation(composer, "5487@247795L244");
                $dirty = $dirty2;
                TextKt.Text--4IGK_g("e.g. Ahmad", (Modifier) null, ColorKt.Color(4288455599L), TextUnitKt.getSp(13), (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 3462, 0, 131058);
                composer = $composer;
            } else {
                $dirty = $dirty2;
                composer.startReplaceGroup(-982289357);
            }
            composer.endReplaceGroup();
            innerTextField.invoke(composer, Integer.valueOf($dirty & 14));
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$965$lambda$964$lambda$963$lambda$961$lambda$960(MutableState $lastName$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $lastName$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Composable
    @ComposableInferredTarget(scheme = "[androidx.compose.ui.UiComposable[androidx.compose.ui.UiComposable]]")
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$965$lambda$964$lambda$963$lambda$962(MutableState $lastName$delegate, Function2 innerTextField, Composer $composer, int $changed) {
        int $dirty;
        Composer composer = $composer;
        Intrinsics.checkNotNullParameter(innerTextField, "innerTextField");
        ComposerKt.sourceInformation(composer, "C5537@250366L16:LiveDhikrScreen.kt#50dkb0");
        int $dirty2 = $changed;
        if (($changed & 6) == 0) {
            $dirty2 |= composer.changedInstance(innerTextField) ? 4 : 2;
        }
        if (($dirty2 & 19) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(883849606, $dirty2, -1, "com.tasbih.digitaltasbih.ui.screens.groups.WelcomeToGroupCampaignsDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:5530)");
            }
            if (WelcomeToGroupCampaignsDialog$lambda$923($lastName$delegate).length() == 0) {
                composer.startReplaceGroup(-1680790055);
                ComposerKt.sourceInformation(composer, "5531@250048L243");
                $dirty = $dirty2;
                TextKt.Text--4IGK_g("e.g. Khan", (Modifier) null, ColorKt.Color(4288455599L), TextUnitKt.getSp(13), (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 3462, 0, 131058);
                composer = $composer;
            } else {
                $dirty = $dirty2;
                composer.startReplaceGroup(-1928807012);
            }
            composer.endReplaceGroup();
            innerTextField.invoke(composer, Integer.valueOf($dirty & 14));
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$972$lambda$968$lambda$967(MutableState $showCountryMenu$delegate) {
        WelcomeToGroupCampaignsDialog$lambda$940($showCountryMenu$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$980$lambda$979$lambda$978$lambda$974$lambda$973(MutableState $state$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $state$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Composable
    @ComposableInferredTarget(scheme = "[androidx.compose.ui.UiComposable[androidx.compose.ui.UiComposable]]")
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$980$lambda$979$lambda$978$lambda$975(MutableState $state$delegate, Function2 innerTextField, Composer $composer, int $changed) {
        int $dirty;
        Composer composer = $composer;
        Intrinsics.checkNotNullParameter(innerTextField, "innerTextField");
        ComposerKt.sourceInformation(composer, "C5643@255515L16:LiveDhikrScreen.kt#50dkb0");
        int $dirty2 = $changed;
        if (($changed & 6) == 0) {
            $dirty2 |= composer.changedInstance(innerTextField) ? 4 : 2;
        }
        if (($dirty2 & 19) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-202565489, $dirty2, -1, "com.tasbih.digitaltasbih.ui.screens.groups.WelcomeToGroupCampaignsDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:5636)");
            }
            if (WelcomeToGroupCampaignsDialog$lambda$930($state$delegate).length() == 0) {
                composer.startReplaceGroup(-199345179);
                ComposerKt.sourceInformation(composer, "5637@255186L254");
                $dirty = $dirty2;
                TextKt.Text--4IGK_g("Type or select State", (Modifier) null, ColorKt.Color(4288455599L), TextUnitKt.getSp(13), (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 3462, 0, 131058);
                composer = $composer;
            } else {
                $dirty = $dirty2;
                composer.startReplaceGroup(-452459373);
            }
            composer.endReplaceGroup();
            innerTextField.invoke(composer, Integer.valueOf($dirty & 14));
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$980$lambda$979$lambda$978$lambda$977$lambda$976(MutableState $showStateMenu$delegate) {
        WelcomeToGroupCampaignsDialog$lambda$943($showStateMenu$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$988$lambda$987$lambda$986$lambda$982$lambda$981(MutableState $city$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $city$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Composable
    @ComposableInferredTarget(scheme = "[androidx.compose.ui.UiComposable[androidx.compose.ui.UiComposable]]")
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$988$lambda$987$lambda$986$lambda$983(MutableState $city$delegate, Function2 innerTextField, Composer $composer, int $changed) {
        int $dirty;
        Composer composer = $composer;
        Intrinsics.checkNotNullParameter(innerTextField, "innerTextField");
        ComposerKt.sourceInformation(composer, "C5706@258701L16:LiveDhikrScreen.kt#50dkb0");
        int $dirty2 = $changed;
        if (($changed & 6) == 0) {
            $dirty2 |= composer.changedInstance(innerTextField) ? 4 : 2;
        }
        if (($dirty2 & 19) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1741406416, $dirty2, -1, "com.tasbih.digitaltasbih.ui.screens.groups.WelcomeToGroupCampaignsDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:5699)");
            }
            if (WelcomeToGroupCampaignsDialog$lambda$933($city$delegate).length() == 0) {
                composer.startReplaceGroup(-671343611);
                ComposerKt.sourceInformation(composer, "5700@258373L253");
                $dirty = $dirty2;
                TextKt.Text--4IGK_g("Type or select City", (Modifier) null, ColorKt.Color(4288455599L), TextUnitKt.getSp(13), (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 3462, 0, 131058);
                composer = $composer;
            } else {
                $dirty = $dirty2;
                composer.startReplaceGroup(-927619278);
            }
            composer.endReplaceGroup();
            innerTextField.invoke(composer, Integer.valueOf($dirty & 14));
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$988$lambda$987$lambda$986$lambda$985$lambda$984(MutableState $showCityMenu$delegate) {
        WelcomeToGroupCampaignsDialog$lambda$946($showCityMenu$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$993$lambda$992$lambda$990$lambda$989(MutableState $consentChecked$delegate, boolean it) {
        WelcomeToGroupCampaignsDialog$lambda$937($consentChecked$delegate, it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$998$lambda$997$lambda$996$lambda$995$lambda$994(GroupCampaignRepository $repository, Function0 $onDismiss, MutableState $firstName$delegate, MutableState $country$delegate, MutableState $state$delegate, MutableState $city$delegate, MutableState $consentChecked$delegate, MutableState $lastName$delegate) {
        if (StringsKt.isBlank(StringsKt.trim(WelcomeToGroupCampaignsDialog$lambda$920($firstName$delegate)).toString())) {
            AppToast.INSTANCE.show("Please enter your First Name to continue.");
            return Unit.INSTANCE;
        }
        if (StringsKt.isBlank(StringsKt.trim(WelcomeToGroupCampaignsDialog$lambda$927($country$delegate)).toString()) && StringsKt.isBlank(StringsKt.trim(WelcomeToGroupCampaignsDialog$lambda$930($state$delegate)).toString()) && StringsKt.isBlank(StringsKt.trim(WelcomeToGroupCampaignsDialog$lambda$933($city$delegate)).toString())) {
            AppToast.INSTANCE.show("Please enter or select at least one: City, State, or Country.");
            return Unit.INSTANCE;
        }
        if (!WelcomeToGroupCampaignsDialog$lambda$936($consentChecked$delegate)) {
            AppToast.INSTANCE.show("Please accept the Public Profile Consent to continue.");
            return Unit.INSTANCE;
        }
        $repository.saveFullProfile(WelcomeToGroupCampaignsDialog$lambda$920($firstName$delegate), WelcomeToGroupCampaignsDialog$lambda$923($lastName$delegate), WelcomeToGroupCampaignsDialog$lambda$927($country$delegate), WelcomeToGroupCampaignsDialog$lambda$930($state$delegate), WelcomeToGroupCampaignsDialog$lambda$933($city$delegate));
        AppToast.INSTANCE.show("Profile saved successfully!");
        $onDismiss.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$1000$lambda$999(MutableState $country$delegate, MutableState $state$delegate, MutableState $city$delegate, MutableState $showCountryMenu$delegate, String selected) {
        Intrinsics.checkNotNullParameter(selected, "selected");
        $country$delegate.setValue(selected);
        $state$delegate.setValue("");
        $city$delegate.setValue("");
        WelcomeToGroupCampaignsDialog$lambda$940($showCountryMenu$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$1002$lambda$1001(MutableState $showCountryMenu$delegate) {
        WelcomeToGroupCampaignsDialog$lambda$940($showCountryMenu$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$1004$lambda$1003(MutableState $state$delegate, MutableState $city$delegate, MutableState $showStateMenu$delegate, String selected) {
        Intrinsics.checkNotNullParameter(selected, "selected");
        $state$delegate.setValue(selected);
        $city$delegate.setValue("");
        WelcomeToGroupCampaignsDialog$lambda$943($showStateMenu$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$1006$lambda$1005(MutableState $showStateMenu$delegate) {
        WelcomeToGroupCampaignsDialog$lambda$943($showStateMenu$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$1008$lambda$1007(MutableState $city$delegate, MutableState $showCityMenu$delegate, String selected) {
        Intrinsics.checkNotNullParameter(selected, "selected");
        $city$delegate.setValue(selected);
        WelcomeToGroupCampaignsDialog$lambda$946($showCityMenu$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit WelcomeToGroupCampaignsDialog$lambda$1010$lambda$1009(MutableState $showCityMenu$delegate) {
        WelcomeToGroupCampaignsDialog$lambda$946($showCityMenu$delegate, false);
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final void LocationSelectionDialog(@NotNull final String title, @NotNull final List<String> list, @NotNull final Function1<? super String, Unit> function1, @NotNull final Function0<Unit> function0, @Nullable Composer $composer, final int $changed) {
        Object obj;
        Object obj2;
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(list, "items");
        Intrinsics.checkNotNullParameter(function1, "onSelect");
        Intrinsics.checkNotNullParameter(function0, "onDismiss");
        Composer $composer2 = $composer.startRestartGroup(-1956658753);
        ComposerKt.sourceInformation($composer2, "C(LocationSelectionDialog)P(3!1,2)5858@264717L31,5859@264768L150,5863@264961L2903,5863@264924L2940:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changed(title) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer2.changedInstance(list) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer2.changedInstance(function1) ? 256 : 128;
        }
        if (($changed & 3072) == 0) {
            $dirty |= $composer2.changedInstance(function0) ? 2048 : 1024;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 1171) == 1170 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1956658753, $dirty2, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LocationSelectionDialog (LiveDhikrScreen.kt:5857)");
            }
            ComposerKt.sourceInformationMarkerStart($composer2, 846619486, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue = $composer2.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = SnapshotStateKt.mutableStateOf$default("", (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer2.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            final MutableState searchQuery$delegate = (MutableState) obj;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            String LocationSelectionDialog$lambda$1013 = LocationSelectionDialog$lambda$1013(searchQuery$delegate);
            ComposerKt.sourceInformationMarkerStart($composer2, 846621237, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            boolean changed = $composer2.changed(LocationSelectionDialog$lambda$1013) | $composer2.changed(list);
            Object rememberedValue2 = $composer2.rememberedValue();
            if (changed || rememberedValue2 == Composer.Companion.getEmpty()) {
                if (StringsKt.isBlank(LocationSelectionDialog$lambda$1013(searchQuery$delegate))) {
                    obj2 = list;
                } else {
                    Collection arrayList = new ArrayList();
                    for (Object obj3 : list) {
                        boolean z = changed;
                        if (StringsKt.contains((String) obj3, StringsKt.trim(LocationSelectionDialog$lambda$1013(searchQuery$delegate)).toString(), true)) {
                            arrayList.add(obj3);
                        }
                        changed = z;
                    }
                    obj2 = (List) arrayList;
                }
                $composer2.updateRememberedValue(obj2);
            } else {
                obj2 = rememberedValue2;
            }
            final List filtered = (List) obj2;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            AndroidDialog_androidKt.Dialog(function0, (DialogProperties) null, ComposableLambdaKt.rememberComposableLambda(2092255126, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda32
                public final Object invoke(Object obj4, Object obj5) {
                    return LiveDhikrScreenKt.LocationSelectionDialog$lambda$1029(filtered, function1, title, function0, searchQuery$delegate, (Composer) obj4, ((Integer) obj5).intValue());
                }
            }, $composer2, 54), $composer2, (($dirty2 >> 9) & 14) | 384, 2);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda33
                public final Object invoke(Object obj4, Object obj5) {
                    return LiveDhikrScreenKt.LocationSelectionDialog$lambda$1030(title, list, function1, function0, $changed, (Composer) obj4, ((Integer) obj5).intValue());
                }
            });
        }
    }

    private static final String LocationSelectionDialog$lambda$1013(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit LocationSelectionDialog$lambda$1029(final List $filtered, final Function1 $onSelect, final String $title, final Function0 $onDismiss, final MutableState $searchQuery$delegate, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C5869@265163L40,5870@265214L2644,5864@264971L2887:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(2092255126, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LocationSelectionDialog.<anonymous> (LiveDhikrScreen.kt:5864)");
            }
            CardKt.Card(SizeKt.heightIn-VpY3zN4$default(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, (Object) null), 0.0f, Dp.constructor-impl(480), 1, (Object) null), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(18)), CardDefaults.INSTANCE.cardColors-ro_MJ88(Color.Companion.getWhite-0d7_KjU(), 0L, 0L, 0L, $composer, (CardDefaults.$stable << 12) | 6, 14), (CardElevation) null, (BorderStroke) null, ComposableLambdaKt.rememberComposableLambda(1567033736, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda70
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return LiveDhikrScreenKt.LocationSelectionDialog$lambda$1029$lambda$1028($filtered, $onSelect, $title, $onDismiss, $searchQuery$delegate, (ColumnScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer, 54), $composer, 196614, 24);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01e8  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x03c0  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x03cc  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0403  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x04f0  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0592  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x05e5  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x04fe  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0419  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x03d2  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01ee  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LocationSelectionDialog$lambda$1029$lambda$1028(final java.util.List r115, final kotlin.jvm.functions.Function1 r116, java.lang.String r117, kotlin.jvm.functions.Function0 r118, final androidx.compose.runtime.MutableState r119, androidx.compose.foundation.layout.ColumnScope r120, androidx.compose.runtime.Composer r121, int r122) {
        /*
            Method dump skipped, instructions count: 1515
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LocationSelectionDialog$lambda$1029$lambda$1028(java.util.List, kotlin.jvm.functions.Function1, java.lang.String, kotlin.jvm.functions.Function0, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LocationSelectionDialog$lambda$1029$lambda$1028$lambda$1027$lambda$1021$lambda$1019$lambda$1018(MutableState $searchQuery$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $searchQuery$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Composable
    @ComposableInferredTarget(scheme = "[androidx.compose.ui.UiComposable[androidx.compose.ui.UiComposable]]")
    public static final Unit LocationSelectionDialog$lambda$1029$lambda$1028$lambda$1027$lambda$1021$lambda$1020(MutableState $searchQuery$delegate, Function2 inner, Composer $composer, int $changed) {
        int $dirty;
        Composer composer = $composer;
        Intrinsics.checkNotNullParameter(inner, "inner");
        ComposerKt.sourceInformation(composer, "C5908@267111L7:LiveDhikrScreen.kt#50dkb0");
        int $dirty2 = $changed;
        if (($changed & 6) == 0) {
            $dirty2 |= composer.changedInstance(inner) ? 4 : 2;
        }
        if (($dirty2 & 19) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1058130497, $dirty2, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LocationSelectionDialog.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (LiveDhikrScreen.kt:5905)");
            }
            if (LocationSelectionDialog$lambda$1013($searchQuery$delegate).length() == 0) {
                composer.startReplaceGroup(2000720163);
                ComposerKt.sourceInformation(composer, "5906@266990L62");
                $dirty = $dirty2;
                TextKt.Text--4IGK_g("Search...", (Modifier) null, ColorKt.Color(4288455599L), TextUnitKt.getSp(13), (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 3462, 0, 131058);
                composer = $composer;
            } else {
                $dirty = $dirty2;
                composer.startReplaceGroup(1735894913);
            }
            composer.endReplaceGroup();
            inner.invoke(composer, Integer.valueOf($dirty & 14));
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LocationSelectionDialog$lambda$1029$lambda$1028$lambda$1027$lambda$1026$lambda$1025(List $filtered, Function1 $onSelect, LazyListScope $this$LazyColumn) {
        Intrinsics.checkNotNullParameter($this$LazyColumn, "$this$LazyColumn");
        $this$LazyColumn.items($filtered.size(), (Function1) null, new LiveDhikrScreenKt$LocationSelectionDialog$lambda$1029$lambda$1028$lambda$1027$lambda$1026$lambda$1025$.inlined.items.default.3(LiveDhikrScreenKt$LocationSelectionDialog$lambda$1029$lambda$1028$lambda$1027$lambda$1026$lambda$1025$.inlined.items.default.1.INSTANCE, $filtered), ComposableLambdaKt.composableLambdaInstance(-632812321, true, new LiveDhikrScreenKt$LocationSelectionDialog$lambda$1029$lambda$1028$lambda$1027$lambda$1026$lambda$1025$.inlined.items.default.4($filtered, $onSelect)));
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    private static final void LeaderboardAdPromptDialog(final String campaignTitle, final Function0<Unit> function0, final Function0<Unit> function02, Composer $composer, final int $changed) {
        final Function0 onDismiss;
        Composer $composer2 = $composer.startRestartGroup(-1607564841);
        ComposerKt.sourceInformation($composer2, "C(LeaderboardAdPromptDialog)P(!1,2)5941@268203L7383,5938@268080L7506:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changed(campaignTitle) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer2.changedInstance(function0) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer2.changedInstance(function02) ? 256 : 128;
        }
        if (($dirty & 147) == 146 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
            onDismiss = function02;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1607564841, $dirty, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LeaderboardAdPromptDialog (LiveDhikrScreen.kt:5937)");
            }
            onDismiss = function02;
            AndroidDialog_androidKt.Dialog(onDismiss, new DialogProperties(false, false, false, 3, (DefaultConstructorMarker) null), ComposableLambdaKt.rememberComposableLambda(-847797408, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda149
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.LeaderboardAdPromptDialog$lambda$1042(campaignTitle, function0, function02, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer2, 54), $composer2, (($dirty >> 6) & 14) | 432, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda150
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.LeaderboardAdPromptDialog$lambda$1043(campaignTitle, function0, onDismiss, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0235  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LeaderboardAdPromptDialog$lambda$1042(final java.lang.String r38, final kotlin.jvm.functions.Function0 r39, final kotlin.jvm.functions.Function0 r40, androidx.compose.runtime.Composer r41, int r42) {
        /*
            Method dump skipped, instructions count: 571
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LeaderboardAdPromptDialog$lambda$1042(java.lang.String, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0d4d  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0e25  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0e31  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0e68  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0fbf  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0e7e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0e37  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0d63 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0d1a  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0b14 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0acb  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x097c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0933  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x07dd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0794  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x06b4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:134:0x066b  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0588 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:137:0x053f  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x03bf  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0251  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x023f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x03ab  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x052d  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0539  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0572  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0659  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0665  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x069e  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0782  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x078e  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x07c7  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0921  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x092d  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0966  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0ab9  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0ac5  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0afe  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0d08  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0d14  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LeaderboardAdPromptDialog$lambda$1042$lambda$1041$lambda$1040(java.lang.String r152, kotlin.jvm.functions.Function0 r153, kotlin.jvm.functions.Function0 r154, androidx.compose.foundation.layout.ColumnScope r155, androidx.compose.runtime.Composer r156, int r157) {
        /*
            Method dump skipped, instructions count: 4037
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LeaderboardAdPromptDialog$lambda$1042$lambda$1041$lambda$1040(java.lang.String, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    private static final void DeleteGroupConfirmDialog(final String campaignTitle, boolean isDarkTheme, final Function0<Unit> function0, final Function0<Unit> function02, Composer $composer, final int $changed, final int i) {
        boolean z;
        final boolean isDarkTheme2;
        final boolean isDarkTheme3;
        Composer $composer2 = $composer.startRestartGroup(189584527);
        ComposerKt.sourceInformation($composer2, "C(DeleteGroupConfirmDialog)P(!2,3)6119@275948L7973,6116@275825L8096:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changed(campaignTitle) ? 4 : 2;
        }
        int i2 = i & 2;
        if (i2 != 0) {
            $dirty |= 48;
            z = isDarkTheme;
        } else if (($changed & 48) == 0) {
            z = isDarkTheme;
            $dirty |= $composer2.changed(z) ? 32 : 16;
        } else {
            z = isDarkTheme;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer2.changedInstance(function0) ? 256 : 128;
        }
        if (($changed & 3072) == 0) {
            $dirty |= $composer2.changedInstance(function02) ? 2048 : 1024;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 1171) == 1170 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
            isDarkTheme3 = z;
        } else {
            if (i2 != 0) {
                isDarkTheme2 = false;
            } else {
                isDarkTheme2 = z;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(189584527, $dirty2, -1, "com.tasbih.digitaltasbih.ui.screens.groups.DeleteGroupConfirmDialog (LiveDhikrScreen.kt:6115)");
            }
            AndroidDialog_androidKt.Dialog(function02, new DialogProperties(false, false, false, 3, (DefaultConstructorMarker) null), ComposableLambdaKt.rememberComposableLambda(-1473407258, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda79
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.DeleteGroupConfirmDialog$lambda$1054(isDarkTheme2, campaignTitle, function0, function02, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer2, 54), $composer2, (($dirty2 >> 9) & 14) | 432, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            isDarkTheme3 = isDarkTheme2;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda80
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.DeleteGroupConfirmDialog$lambda$1055(campaignTitle, isDarkTheme3, function0, function02, $changed, i, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0242  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x018e  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit DeleteGroupConfirmDialog$lambda$1054(final boolean r40, final java.lang.String r41, final kotlin.jvm.functions.Function0 r42, final kotlin.jvm.functions.Function0 r43, androidx.compose.runtime.Composer r44, int r45) {
        /*
            Method dump skipped, instructions count: 584
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.DeleteGroupConfirmDialog$lambda$1054(boolean, java.lang.String, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0b3e  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0b4a  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0b83  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0c3e  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0d4b  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0d57  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0d8e  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0e94  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0f07  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0e9a  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0da4  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0d5d  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0b99 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0b50  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0a81  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x09d8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:140:0x098f  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x08bc  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0815 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x07cc  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x06f0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x06a7  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x05c2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0579  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x04e1  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x04bb  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0437  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x03d1  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x03bc  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x024c  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0246  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x03a8  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x03ce  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0431  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x04b3  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x04d9  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0567  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0573  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x05ac  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0695  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x06a1  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x06da  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x07ba  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x07c6  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x07ff  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x08b7  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x097d  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0989  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x09c2  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0a7c  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit DeleteGroupConfirmDialog$lambda$1054$lambda$1053$lambda$1052(java.lang.String r133, boolean r134, kotlin.jvm.functions.Function0 r135, kotlin.jvm.functions.Function0 r136, androidx.compose.foundation.layout.ColumnScope r137, androidx.compose.runtime.Composer r138, int r139) {
        /*
            Method dump skipped, instructions count: 3853
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.DeleteGroupConfirmDialog$lambda$1054$lambda$1053$lambda$1052(java.lang.String, boolean, kotlin.jvm.functions.Function0, kotlin.jvm.functions.Function0, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    private static final void LeaderboardModalSheet(CampaignItem campaign, String campaignId, final GroupCampaignRepository repository, final Function0<Unit> function0, Composer $composer, final int $changed, final int i) {
        CampaignItem campaignItem;
        String str;
        final Function0<Unit> function02;
        int $dirty;
        String campaignId2;
        CampaignItem campaign2;
        String campaignId3;
        Object obj;
        final MutableState participants$delegate;
        boolean z;
        Object obj2;
        Object obj3;
        String str2;
        CampaignItem campaign3;
        String campaignId4;
        Object obj4;
        long j;
        long totalRecited;
        long target;
        final int pct;
        final String campaignId5;
        final CampaignItem campaign4;
        int i2;
        Composer $composer2 = $composer.startRestartGroup(-1862590512);
        ComposerKt.sourceInformation($composer2, "C(LeaderboardModalSheet)P(!2,3)6295@284225L63,6296@284310L33,6297@284365L37,6298@284420L55,6300@284508L107,6300@284481L134,6316@285071L25407,6309@284845L25633:LiveDhikrScreen.kt#50dkb0");
        int $dirty2 = $changed;
        int i3 = i & 1;
        if (i3 != 0) {
            $dirty2 |= 6;
            campaignItem = campaign;
        } else if (($changed & 6) == 0) {
            campaignItem = campaign;
            $dirty2 |= $composer2.changed(campaignItem) ? 4 : 2;
        } else {
            campaignItem = campaign;
        }
        if (($changed & 48) == 0) {
            if ((i & 2) == 0) {
                str = campaignId;
                if ($composer2.changed(str)) {
                    i2 = 32;
                    $dirty2 |= i2;
                }
            } else {
                str = campaignId;
            }
            i2 = 16;
            $dirty2 |= i2;
        } else {
            str = campaignId;
        }
        if (($changed & 384) == 0) {
            $dirty2 |= $composer2.changedInstance(repository) ? 256 : 128;
        }
        if (($changed & 3072) == 0) {
            function02 = function0;
            $dirty2 |= $composer2.changedInstance(function02) ? 2048 : 1024;
        } else {
            function02 = function0;
        }
        if (($dirty2 & 1171) == 1170 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
            campaignId5 = str;
            campaign4 = campaignItem;
        } else {
            $composer2.startDefaults();
            if (($changed & 1) == 0 || $composer2.getDefaultsInvalid()) {
                CampaignItem campaign5 = i3 != 0 ? null : campaignItem;
                if ((i & 2) != 0) {
                    if (campaign5 == null || (campaignId3 = campaign5.getCampaignId()) == null) {
                        campaignId3 = "";
                    }
                    $dirty = $dirty2 & (-113);
                    campaign2 = campaign5;
                    campaignId2 = campaignId3;
                } else {
                    String str3 = str;
                    $dirty = $dirty2;
                    campaignId2 = str3;
                    campaign2 = campaign5;
                }
            } else {
                $composer2.skipToGroupEnd();
                if ((i & 2) != 0) {
                    $dirty2 &= -113;
                }
                String str4 = str;
                $dirty = $dirty2;
                campaignId2 = str4;
                campaign2 = campaignItem;
            }
            $composer2.endDefaults();
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1862590512, $dirty, -1, "com.tasbih.digitaltasbih.ui.screens.groups.LeaderboardModalSheet (LiveDhikrScreen.kt:6294)");
            }
            ComposerKt.sourceInformationMarkerStart($composer2, 1051887855, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue = $composer2.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = SnapshotStateKt.mutableStateOf$default(CollectionsKt.emptyList(), (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer2.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            MutableState participants$delegate2 = (MutableState) obj;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 1051890545, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue2 = $composer2.rememberedValue();
            if (rememberedValue2 == Composer.Companion.getEmpty()) {
                z = true;
                participants$delegate = participants$delegate2;
                obj2 = SnapshotStateKt.mutableStateOf$default(true, (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer2.updateRememberedValue(obj2);
            } else {
                participants$delegate = participants$delegate2;
                z = true;
                obj2 = rememberedValue2;
            }
            final MutableState isLoading$delegate = (MutableState) obj2;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 1051892309, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue3 = $composer2.rememberedValue();
            if (rememberedValue3 == Composer.Companion.getEmpty()) {
                obj3 = repository.getDeviceId();
                $composer2.updateRememberedValue(obj3);
            } else {
                obj3 = rememberedValue3;
            }
            final String myDeviceId = (String) obj3;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 1051894087, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            Object rememberedValue4 = $composer2.rememberedValue();
            if (rememberedValue4 == Composer.Companion.getEmpty()) {
                String userName = repository.getUserName();
                if (StringsKt.isBlank(userName)) {
                    userName = "You";
                }
                str2 = userName;
                $composer2.updateRememberedValue(str2);
            } else {
                str2 = rememberedValue4;
            }
            final String myName = (String) str2;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 1051896955, "CC(remember):LiveDhikrScreen.kt#9igjgp");
            boolean changedInstance = $composer2.changedInstance(repository) | ((((($dirty & 112) ^ 48) <= 32 || !$composer2.changed(campaignId2)) && ($dirty & 48) != 32) ? false : z) | (($dirty & 14) == 4 ? z : false);
            Object rememberedValue5 = $composer2.rememberedValue();
            if (changedInstance || rememberedValue5 == Composer.Companion.getEmpty()) {
                campaign3 = campaign2;
                campaignId4 = campaignId2;
                obj4 = (Function2) new LeaderboardModalSheet.1.1(repository, campaignId2, campaign3, participants$delegate, isLoading$delegate, (Continuation) null);
                $composer2.updateRememberedValue(obj4);
            } else {
                obj4 = rememberedValue5;
                campaign3 = campaign2;
                campaignId4 = campaignId2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer2);
            EffectsKt.LaunchedEffect(campaignId4, (Function2) obj4, $composer2, ($dirty >> 3) & 14);
            if (campaign3 != null) {
                j = campaign3.getTotalCount();
            } else {
                Iterator<T> it = LeaderboardModalSheet$lambda$1057(participants$delegate).iterator();
                j = 0;
                while (it.hasNext()) {
                    j += ((ParticipantItem) it.next()).getCount();
                }
            }
            long totalRecited2 = j;
            long target2 = campaign3 != null ? campaign3.getTarget() : 0L;
            if (target2 > 0) {
                totalRecited = totalRecited2;
                target = target2;
                pct = RangesKt.coerceIn((int) ((((float) totalRecited2) / ((float) target)) * 100.0f), 0, 100);
            } else {
                totalRecited = totalRecited2;
                target = target2;
                pct = 0;
            }
            boolean z2 = z;
            final CampaignItem campaign6 = campaign3;
            final long totalRecited3 = totalRecited;
            final long totalRecited4 = target;
            AndroidDialog_androidKt.Dialog(function0, new DialogProperties(z2, z2, false), ComposableLambdaKt.rememberComposableLambda(-1848486873, z2, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda97
                public final Object invoke(Object obj5, Object obj6) {
                    return LiveDhikrScreenKt.LeaderboardModalSheet$lambda$1104(campaign6, function02, totalRecited3, totalRecited4, pct, participants$delegate, isLoading$delegate, myDeviceId, myName, (Composer) obj5, ((Integer) obj6).intValue());
                }
            }, $composer2, 54), $composer2, (($dirty >> 9) & 14) | 432, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            campaignId5 = campaignId4;
            campaign4 = campaign6;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda98
                public final Object invoke(Object obj5, Object obj6) {
                    return LiveDhikrScreenKt.LeaderboardModalSheet$lambda$1105(campaign4, campaignId5, repository, function0, $changed, i, (Composer) obj5, ((Integer) obj6).intValue());
                }
            });
        }
    }

    private static final List<ParticipantItem> LeaderboardModalSheet$lambda$1057(MutableState<List<ParticipantItem>> mutableState) {
        return (List) ((State) mutableState).getValue();
    }

    private static final boolean LeaderboardModalSheet$lambda$1060(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void LeaderboardModalSheet$lambda$1061(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0241  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LeaderboardModalSheet$lambda$1104(final com.tasbih.digitaltasbih.data.groups.CampaignItem r51, final kotlin.jvm.functions.Function0 r52, final long r53, final long r55, final int r57, final androidx.compose.runtime.MutableState r58, final androidx.compose.runtime.MutableState r59, final java.lang.String r60, final java.lang.String r61, androidx.compose.runtime.Composer r62, int r63) {
        /*
            Method dump skipped, instructions count: 583
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LeaderboardModalSheet$lambda$1104(com.tasbih.digitaltasbih.data.groups.CampaignItem, kotlin.jvm.functions.Function0, long, long, int, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, java.lang.String, java.lang.String, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0c3c  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0c48  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0d6a  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0d76  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0daf  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0e96  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0ea2  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0edb  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0f4c  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x103d  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x1061  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x10f7  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x1103  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x113c  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x11a3  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x11b8  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x12b0  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x12bc  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x12f5  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x14c9  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x14d5  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x150e  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x16b0  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x16bc  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x16f5  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x17de  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x1975  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x1981  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x20b3  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x20bf  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x20f8  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x2247  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x2253  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x228a  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x2375  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x22a0  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x2259  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x210e  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x20c5  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x1987  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x1ae2  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x022c  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x1ce1  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x1ced  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x1dfe  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x1e0a  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x1e43  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x1e59 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:295:0x1e10  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x1cf3  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x170b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:311:0x16c2  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x1524 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:314:0x14db  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x130b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:317:0x12c2  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x11be  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x11a6  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x1152 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:322:0x1109  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x1067  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x1043  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x0fb5  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x0ef1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:328:0x0ea8  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x0dc5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:331:0x0d7c  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x0c4e  */
    /* JADX WARN: Removed duplicated region for block: B:338:0x162d  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x0978 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:341:0x092f  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x07ad A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:344:0x0764  */
    /* JADX WARN: Removed duplicated region for block: B:346:0x0639 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:347:0x05f0  */
    /* JADX WARN: Removed duplicated region for block: B:349:0x04de A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:350:0x0495  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x03b0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:353:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:356:0x023e  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0355  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0361  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x039a  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0483  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x048f  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x04c8  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x05de  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x05ea  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0623  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0752  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x075e  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0797  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x091d  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0929  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0962  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0a3d  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LeaderboardModalSheet$lambda$1104$lambda$1103$lambda$1102(final com.tasbih.digitaltasbih.data.groups.CampaignItem r158, kotlin.jvm.functions.Function0 r159, long r160, long r162, int r164, final androidx.compose.runtime.MutableState r165, androidx.compose.runtime.MutableState r166, final java.lang.String r167, final java.lang.String r168, androidx.compose.foundation.layout.ColumnScope r169, androidx.compose.runtime.Composer r170, int r171) {
        /*
            Method dump skipped, instructions count: 9083
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LeaderboardModalSheet$lambda$1104$lambda$1103$lambda$1102(com.tasbih.digitaltasbih.data.groups.CampaignItem, kotlin.jvm.functions.Function0, long, long, int, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, java.lang.String, java.lang.String, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LeaderboardModalSheet$lambda$1104$lambda$1103$lambda$1102$lambda$1101$lambda$1098$lambda$1097(final MutableState $participants$delegate, final String $myDeviceId, final String $myName, final CampaignItem $campaign, LazyListScope $this$LazyColumn) {
        Intrinsics.checkNotNullParameter($this$LazyColumn, "$this$LazyColumn");
        LazyListScope.items$default($this$LazyColumn, LeaderboardModalSheet$lambda$1057($participants$delegate).size(), (Function1) null, (Function1) null, ComposableLambdaKt.composableLambdaInstance(1393953677, true, new Function4() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda190
            public final Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
                return LiveDhikrScreenKt.LeaderboardModalSheet$lambda$1104$lambda$1103$lambda$1102$lambda$1101$lambda$1098$lambda$1097$lambda$1096($myDeviceId, $myName, $campaign, $participants$delegate, (LazyItemScope) obj, ((Integer) obj2).intValue(), (Composer) obj3, ((Integer) obj4).intValue());
            }
        }), 6, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x006b, code lost:
    
        if (kotlin.jvm.internal.Intrinsics.areEqual(r5.getDeviceId(), r179) != false) goto L32;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x080f  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0848  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0983  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x098f  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x09c8  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0aa5  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0ab1  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0aea  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0b95  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0f6a  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x107b  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x1087  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x10be  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x11e0  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x10d4  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x108d  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0fdc  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0d59  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x0b00 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0ab7  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x09de A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0995  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x085e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0815  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x06ef  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0719  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0743  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0603  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x05ac A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0563  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0495 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:205:0x044c  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0361 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0318  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x00e3  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x01d1  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x01dd  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0306  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0312  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x034b  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x043a  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0446  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x047f  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0551  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x055d  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0596  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x05fc  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x06b0  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x06c9  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0803  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LeaderboardModalSheet$lambda$1104$lambda$1103$lambda$1102$lambda$1101$lambda$1098$lambda$1097$lambda$1096(java.lang.String r179, java.lang.String r180, com.tasbih.digitaltasbih.data.groups.CampaignItem r181, androidx.compose.runtime.MutableState r182, androidx.compose.foundation.lazy.LazyItemScope r183, int r184, androidx.compose.runtime.Composer r185, int r186) {
        /*
            Method dump skipped, instructions count: 4602
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.LeaderboardModalSheet$lambda$1104$lambda$1103$lambda$1102$lambda$1101$lambda$1098$lambda$1097$lambda$1096(java.lang.String, java.lang.String, com.tasbih.digitaltasbih.data.groups.CampaignItem, androidx.compose.runtime.MutableState, androidx.compose.foundation.lazy.LazyItemScope, int, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    private static final void FazilatModalSheet(final String title, final Function0<Unit> function0, Composer $composer, final int $changed) {
        final Function0 onDismiss;
        Composer $composer2 = $composer.startRestartGroup(-1216347248);
        ComposerKt.sourceInformation($composer2, "C(FazilatModalSheet)P(1)6771@310654L2511,6771@310617L2548:LiveDhikrScreen.kt#50dkb0");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changed(title) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer2.changedInstance(function0) ? 32 : 16;
        }
        if (($dirty & 19) == 18 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
            onDismiss = function0;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1216347248, $dirty, -1, "com.tasbih.digitaltasbih.ui.screens.groups.FazilatModalSheet (LiveDhikrScreen.kt:6770)");
            }
            onDismiss = function0;
            AndroidDialog_androidKt.Dialog(onDismiss, (DialogProperties) null, ComposableLambdaKt.rememberComposableLambda(1955112281, true, new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda24
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.FazilatModalSheet$lambda$1109(title, function0, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer2, 54), $composer2, (($dirty >> 3) & 14) | 384, 2);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda25
                public final Object invoke(Object obj, Object obj2) {
                    return LiveDhikrScreenKt.FazilatModalSheet$lambda$1110(title, onDismiss, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit FazilatModalSheet$lambda$1109(final String $title, final Function0 $onDismiss, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C6777@310859L39,6779@311012L39,6780@311062L2097,6772@310664L2495:LiveDhikrScreen.kt#50dkb0");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1955112281, $changed, -1, "com.tasbih.digitaltasbih.ui.screens.groups.FazilatModalSheet.<anonymous> (LiveDhikrScreen.kt:6772)");
            }
            CardKt.Card(PaddingKt.padding-VpY3zN4$default(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, (Object) null), 0.0f, Dp.constructor-impl(16), 1, (Object) null), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(24)), CardDefaults.INSTANCE.cardColors-ro_MJ88(DarkCardBg, 0L, 0L, 0L, $composer, (CardDefaults.$stable << 12) | 6, 14), CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl(16), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, $composer, (CardDefaults.$stable << 18) | 6, 62), BorderStrokeKt.BorderStroke-cXLIe8U(Dp.constructor-impl((float) 1.5d), Color.copy-wmQWz5c$default(ShimmerGold, 0.6f, 0.0f, 0.0f, 0.0f, 14, (Object) null)), ComposableLambdaKt.rememberComposableLambda(-127840217, true, new Function3() { // from class: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt$$ExternalSyntheticLambda260
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return LiveDhikrScreenKt.FazilatModalSheet$lambda$1109$lambda$1108($title, $onDismiss, (ColumnScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer, 54), $composer, 221190, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01e2  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x042d  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x01e8  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit FazilatModalSheet$lambda$1109$lambda$1108(java.lang.String r84, kotlin.jvm.functions.Function0 r85, androidx.compose.foundation.layout.ColumnScope r86, androidx.compose.runtime.Composer r87, int r88) {
        /*
            Method dump skipped, instructions count: 1075
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tasbih.digitaltasbih.ui.screens.groups.LiveDhikrScreenKt.FazilatModalSheet$lambda$1109$lambda$1108(java.lang.String, kotlin.jvm.functions.Function0, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }
}
