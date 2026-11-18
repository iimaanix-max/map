.class public final synthetic Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;
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

.field public final synthetic f$1:Ljava/lang/String;

.field public final synthetic f$2:Landroidx/compose/ui/graphics/vector/ImageVector;

.field public final synthetic f$3:Z

.field public final synthetic f$4:Z

.field public final synthetic f$5:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$6:I

.field public final synthetic f$7:I


# direct methods
.method public synthetic constructor <init>(Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;ZZLkotlin/jvm/functions/Function0;II)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$0:Ljava/lang/String;

    iput-object p2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$1:Ljava/lang/String;

    iput-object p3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$2:Landroidx/compose/ui/graphics/vector/ImageVector;

    iput-boolean p4, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$3:Z

    iput-boolean p5, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$4:Z

    iput-object p6, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$5:Lkotlin/jvm/functions/Function0;

    iput p7, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$6:I

    iput p8, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$7:I

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 10

    .line 0
    iget-object v0, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$0:Ljava/lang/String;

    iget-object v1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$1:Ljava/lang/String;

    iget-object v2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$2:Landroidx/compose/ui/graphics/vector/ImageVector;

    iget-boolean v3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$3:Z

    iget-boolean v4, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$4:Z

    iget-object v5, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$5:Lkotlin/jvm/functions/Function0;

    iget v6, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$6:I

    iget v7, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda63;->f$7:I

    move-object v8, p1

    check-cast v8, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v9

    invoke-static/range {v0 .. v9}, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt;->PillButton$lambda$665(Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;ZZLkotlin/jvm/functions/Function0;IILandroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
