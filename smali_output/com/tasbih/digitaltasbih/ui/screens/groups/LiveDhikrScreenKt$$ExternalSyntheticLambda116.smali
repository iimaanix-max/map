.class public final synthetic Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function0;


# annotations
.annotation build Lcom/android/tools/r8/annotations/SynthesizedClassV2;
    apiLevel = -0x2
    kind = 0x13
    versionHash = "7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8"
.end annotation


# instance fields
.field public final synthetic f$0:I

.field public final synthetic f$1:J

.field public final synthetic f$2:Lkotlinx/coroutines/CoroutineScope;

.field public final synthetic f$3:Landroidx/compose/runtime/MutableIntState;

.field public final synthetic f$4:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$5:Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;

.field public final synthetic f$6:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

.field public final synthetic f$7:Z

.field public final synthetic f$8:Landroidx/compose/runtime/MutableLongState;


# direct methods
.method public synthetic constructor <init>(IJLkotlinx/coroutines/CoroutineScope;Landroidx/compose/runtime/MutableIntState;Landroidx/compose/runtime/MutableState;Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;ZLandroidx/compose/runtime/MutableLongState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$0:I

    iput-wide p2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$1:J

    iput-object p4, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$2:Lkotlinx/coroutines/CoroutineScope;

    iput-object p5, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$3:Landroidx/compose/runtime/MutableIntState;

    iput-object p6, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$4:Landroidx/compose/runtime/MutableState;

    iput-object p7, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$5:Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;

    iput-object p8, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$6:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

    iput-boolean p9, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$7:Z

    iput-object p10, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$8:Landroidx/compose/runtime/MutableLongState;

    return-void
.end method


# virtual methods
.method public final invoke()Ljava/lang/Object;
    .locals 10

    .line 0
    iget v0, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$0:I

    iget-wide v1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$1:J

    iget-object v3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$2:Lkotlinx/coroutines/CoroutineScope;

    iget-object v4, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$3:Landroidx/compose/runtime/MutableIntState;

    iget-object v5, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$4:Landroidx/compose/runtime/MutableState;

    iget-object v6, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$5:Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;

    iget-object v7, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$6:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

    iget-boolean v8, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$7:Z

    iget-object v9, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda116;->f$8:Landroidx/compose/runtime/MutableLongState;

    invoke-static/range {v0 .. v9}, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt;->LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$547$lambda$542$lambda$541(IJLkotlinx/coroutines/CoroutineScope;Landroidx/compose/runtime/MutableIntState;Landroidx/compose/runtime/MutableState;Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;ZLandroidx/compose/runtime/MutableLongState;)Lkotlin/Unit;

    move-result-object v0

    return-object v0
.end method
