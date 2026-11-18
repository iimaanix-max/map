.class public final synthetic Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;
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
.field public final synthetic f$0:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$1:I

.field public final synthetic f$2:J

.field public final synthetic f$3:Lkotlinx/coroutines/CoroutineScope;

.field public final synthetic f$4:Landroidx/compose/runtime/MutableIntState;

.field public final synthetic f$5:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$6:Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;

.field public final synthetic f$7:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

.field public final synthetic f$8:Z

.field public final synthetic f$9:Landroidx/compose/runtime/MutableLongState;


# direct methods
.method public synthetic constructor <init>(Lkotlin/jvm/functions/Function0;IJLkotlinx/coroutines/CoroutineScope;Landroidx/compose/runtime/MutableIntState;Landroidx/compose/runtime/MutableState;Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;ZLandroidx/compose/runtime/MutableLongState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$0:Lkotlin/jvm/functions/Function0;

    iput p2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$1:I

    iput-wide p3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$2:J

    iput-object p5, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$3:Lkotlinx/coroutines/CoroutineScope;

    iput-object p6, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$4:Landroidx/compose/runtime/MutableIntState;

    iput-object p7, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$5:Landroidx/compose/runtime/MutableState;

    iput-object p8, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$6:Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;

    iput-object p9, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$7:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

    iput-boolean p10, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$8:Z

    iput-object p11, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$9:Landroidx/compose/runtime/MutableLongState;

    return-void
.end method


# virtual methods
.method public final invoke()Ljava/lang/Object;
    .locals 11

    .line 0
    iget-object v0, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$0:Lkotlin/jvm/functions/Function0;

    iget v1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$1:I

    iget-wide v2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$2:J

    iget-object v4, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$3:Lkotlinx/coroutines/CoroutineScope;

    iget-object v5, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$4:Landroidx/compose/runtime/MutableIntState;

    iget-object v6, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$5:Landroidx/compose/runtime/MutableState;

    iget-object v7, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$6:Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;

    iget-object v8, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$7:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

    iget-boolean v9, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$8:Z

    iget-object v10, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda110;->f$9:Landroidx/compose/runtime/MutableLongState;

    invoke-static/range {v0 .. v10}, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt;->LiveActiveDhikrRoom$lambda$456$lambda$455(Lkotlin/jvm/functions/Function0;IJLkotlinx/coroutines/CoroutineScope;Landroidx/compose/runtime/MutableIntState;Landroidx/compose/runtime/MutableState;Lcom/tasbih/digitaltasbih/data/groups/GroupCampaignRepository;Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;ZLandroidx/compose/runtime/MutableLongState;)Lkotlin/Unit;

    move-result-object v0

    return-object v0
.end method
