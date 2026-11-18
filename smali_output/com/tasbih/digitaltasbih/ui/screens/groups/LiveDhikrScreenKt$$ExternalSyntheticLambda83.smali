.class public final synthetic Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;
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

.field public final synthetic f$1:Z

.field public final synthetic f$2:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$3:J

.field public final synthetic f$4:J

.field public final synthetic f$5:J

.field public final synthetic f$6:J

.field public final synthetic f$7:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$8:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$9:J


# direct methods
.method public synthetic constructor <init>(Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;ZLkotlin/jvm/functions/Function0;JJJJLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;J)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$0:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

    iput-boolean p2, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$1:Z

    iput-object p3, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$2:Lkotlin/jvm/functions/Function0;

    iput-wide p4, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$3:J

    iput-wide p6, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$4:J

    iput-wide p8, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$5:J

    iput-wide p10, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$6:J

    iput-object p12, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$7:Lkotlin/jvm/functions/Function0;

    iput-object p13, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$8:Lkotlin/jvm/functions/Function0;

    iput-wide p14, p0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$9:J

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 19

    .line 0
    move-object/from16 v0, p0

    iget-object v1, v0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$0:Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;

    iget-boolean v2, v0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$1:Z

    iget-object v3, v0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$2:Lkotlin/jvm/functions/Function0;

    iget-wide v4, v0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$3:J

    iget-wide v6, v0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$4:J

    iget-wide v8, v0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$5:J

    iget-wide v10, v0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$6:J

    iget-object v12, v0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$7:Lkotlin/jvm/functions/Function0;

    iget-object v13, v0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$8:Lkotlin/jvm/functions/Function0;

    iget-wide v14, v0, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt$$ExternalSyntheticLambda83;->f$9:J

    move-object/from16 v16, p1

    check-cast v16, Landroidx/compose/foundation/layout/ColumnScope;

    move-object/from16 v17, p2

    check-cast v17, Landroidx/compose/runtime/Composer;

    move-object/from16 v18, p3

    check-cast v18, Ljava/lang/Integer;

    invoke-virtual/range {v18 .. v18}, Ljava/lang/Integer;->intValue()I

    move-result v18

    invoke-static/range {v1 .. v18}, Lcom/tasbih/digitaltasbih/ui/screens/groups/LiveDhikrScreenKt;->GrandUmrahMegaPoster$lambda$307(Lcom/tasbih/digitaltasbih/data/groups/CampaignItem;ZLkotlin/jvm/functions/Function0;JJJJLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;JLandroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object v1

    return-object v1
.end method
