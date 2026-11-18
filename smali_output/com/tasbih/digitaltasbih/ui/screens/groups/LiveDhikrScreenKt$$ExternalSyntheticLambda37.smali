.class public final synthetic Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda37;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# annotations
.annotation build Lcom/android/tools/r8/annotations/SynthesizedClassV2;
    apiLevel = -0x2
    kind = 0x13
    versionHash = "7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8"
.end annotation


# instance fields
.field public final synthetic f$0:Ljava/lang/String;

.field public final synthetic f$1:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$2:Lkotlin/jvm/functions/Function0;


# direct methods
.method public synthetic constructor <init>(Ljava/lang/String;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda37;->f$0:Ljava/lang/String;

    iput-object p2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda37;->f$1:Lkotlin/jvm/functions/Function0;

    iput-object p3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda37;->f$2:Lkotlin/jvm/functions/Function0;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6

    .line 0
    iget-object v0, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda37;->f$0:Ljava/lang/String;

    iget-object v1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda37;->f$1:Lkotlin/jvm/functions/Function0;

    iget-object v2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda37;->f$2:Lkotlin/jvm/functions/Function0;

    move-object v3, p1

    check-cast v3, Landroidx/compose/foundation/layout/ColumnScope;

    move-object v4, p2

    check-cast v4, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result v5

    invoke-static/range {v0 .. v5}, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt;->LeaderboardAdPromptDialog$lambda$1042$lambda$1041$lambda$1040(Ljava/lang/String;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Landroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
