.class public final synthetic Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda146;
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
.field public final synthetic f$0:Landroid/content/Context;

.field public final synthetic f$1:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

.field public final synthetic f$2:Z

.field public final synthetic f$3:Ljava/lang/String;


# direct methods
.method public synthetic constructor <init>(Landroid/content/Context;Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;ZLjava/lang/String;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda146;->f$0:Landroid/content/Context;

    iput-object p2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda146;->f$1:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

    iput-boolean p3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda146;->f$2:Z

    iput-object p4, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda146;->f$3:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public final invoke()Ljava/lang/Object;
    .locals 4

    .line 0
    iget-object v0, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda146;->f$0:Landroid/content/Context;

    iget-object v1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda146;->f$1:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

    iget-boolean v2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda146;->f$2:Z

    iget-object v3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda146;->f$3:Ljava/lang/String;

    invoke-static {v0, v1, v2, v3}, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt;->LiveActiveDhikrRoom$lambda$610$lambda$577$lambda$576$lambda$575$lambda$570$lambda$564$lambda$563(Landroid/content/Context;Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;ZLjava/lang/String;)Lkotlin/Unit;

    move-result-object v0

    return-object v0
.end method
