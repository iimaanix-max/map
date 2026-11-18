.class public final synthetic Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda31;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function1;


# annotations
.annotation build Lcom/android/tools/r8/annotations/SynthesizedClassV2;
    apiLevel = -0x2
    kind = 0x13
    versionHash = "7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8"
.end annotation


# instance fields
.field public final synthetic f$0:Ljava/util/List;

.field public final synthetic f$1:Z

.field public final synthetic f$2:Lkotlin/jvm/functions/Function1;


# direct methods
.method public synthetic constructor <init>(Ljava/util/List;ZLkotlin/jvm/functions/Function1;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda31;->f$0:Ljava/util/List;

    iput-boolean p2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda31;->f$1:Z

    iput-object p3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda31;->f$2:Lkotlin/jvm/functions/Function1;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 3

    .line 0
    iget-object v0, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda31;->f$0:Ljava/util/List;

    iget-boolean v1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda31;->f$1:Z

    iget-object v2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda31;->f$2:Lkotlin/jvm/functions/Function1;

    check-cast p1, Landroidx/compose/foundation/lazy/LazyListScope;

    invoke-static {v0, v1, v2, p1}, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt;->DhikrPickerSheet$lambda$781$lambda$780$lambda$779$lambda$778(Ljava/util/List;ZLkotlin/jvm/functions/Function1;Landroidx/compose/foundation/lazy/LazyListScope;)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
