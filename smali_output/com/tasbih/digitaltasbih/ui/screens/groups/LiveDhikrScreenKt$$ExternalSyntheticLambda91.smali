.class public final synthetic Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;
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
.field public final synthetic f$0:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

.field public final synthetic f$1:Landroid/content/Context;

.field public final synthetic f$2:Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;

.field public final synthetic f$3:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$4:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$5:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$6:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$7:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;Landroid/content/Context;Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$0:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

    iput-object p2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$1:Landroid/content/Context;

    iput-object p3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$2:Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;

    iput-object p4, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$3:Landroidx/compose/runtime/MutableState;

    iput-object p5, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$4:Landroidx/compose/runtime/MutableState;

    iput-object p6, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$5:Landroidx/compose/runtime/MutableState;

    iput-object p7, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$6:Landroidx/compose/runtime/MutableState;

    iput-object p8, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$7:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 11

    .line 0
    iget-object v0, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$0:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

    iget-object v1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$1:Landroid/content/Context;

    iget-object v2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$2:Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;

    iget-object v3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$3:Landroidx/compose/runtime/MutableState;

    iget-object v4, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$4:Landroidx/compose/runtime/MutableState;

    iget-object v5, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$5:Landroidx/compose/runtime/MutableState;

    iget-object v6, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$6:Landroidx/compose/runtime/MutableState;

    iget-object v7, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda91;->f$7:Landroidx/compose/runtime/MutableState;

    move-object v8, p1

    check-cast v8, Landroidx/compose/foundation/lazy/LazyItemScope;

    move-object v9, p2

    check-cast v9, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result v10

    invoke-static/range {v0 .. v10}, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt;->LiveDhikrScreen$lambda$278$lambda$222$lambda$221$lambda$220$lambda$200(Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;Landroid/content/Context;Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/foundation/lazy/LazyItemScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
