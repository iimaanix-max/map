.class public final synthetic Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda224;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# annotations
.annotation build Lcom/android/tools/r8/annotations/SynthesizedClassV2;
    apiLevel = -0x2
    kind = 0x13
    versionHash = "7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8"
.end annotation


# instance fields
.field public final synthetic f$0:Ljava/lang/String;

.field public final synthetic f$1:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$2:Landroidx/compose/ui/Modifier;

.field public final synthetic f$3:I

.field public final synthetic f$4:I


# direct methods
.method public synthetic constructor <init>(Ljava/lang/String;Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;II)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda224;->f$0:Ljava/lang/String;

    iput-object p2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda224;->f$1:Lkotlin/jvm/functions/Function0;

    iput-object p3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda224;->f$2:Landroidx/compose/ui/Modifier;

    iput p4, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda224;->f$3:I

    iput p5, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda224;->f$4:I

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 7

    .line 0
    iget-object v0, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda224;->f$0:Ljava/lang/String;

    iget-object v1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda224;->f$1:Lkotlin/jvm/functions/Function0;

    iget-object v2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda224;->f$2:Landroidx/compose/ui/Modifier;

    iget v3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda224;->f$3:I

    iget v4, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda224;->f$4:I

    move-object v5, p1

    check-cast v5, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v6

    invoke-static/range {v0 .. v6}, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt;->LiveDhikrScreen$lambda$94(Ljava/lang/String;Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;IILandroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
