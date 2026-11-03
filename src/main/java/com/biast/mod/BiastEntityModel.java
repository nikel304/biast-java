package com.biast.mod;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

/** Distinct silhouettes for the three BIAST hunters. */
public class BiastEntityModel<T extends Mob> extends HierarchicalModel<T> {
    public static final ModelLayerLocation OBSERVER = layer("observer");
    public static final ModelLayerLocation CUBED = layer("cubedhexa22");
    public static final ModelLayerLocation ASHLE = layer("ashle");
    public static final ModelLayerLocation SLACKER = layer("slacker");
    private static ModelLayerLocation layer(String name) { return new ModelLayerLocation(new ResourceLocation(BiastMod.MODID, name), "main"); }

    private final ModelPart root, head, leftArm, rightArm, leftLeg, rightLeg;
    public BiastEntityModel(ModelPart root) {
        this.root = root; this.head = root.getChild("head");
        this.leftArm = root.getChild("left_arm"); this.rightArm = root.getChild("right_arm");
        this.leftLeg = root.getChild("left_leg"); this.rightLeg = root.getChild("right_leg");
    }
    public static LayerDefinition createLayer(int variant) {
        MeshDefinition mesh = new MeshDefinition(); PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16,16).addBox(-4,-6,-2,8,12,4), PartPose.offset(0,12,0));
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0,0).addBox(-4,-8,-4,8,8,8), PartPose.offset(0,4,0));
        if (variant == 0) { // Observer: tall sensor crown and side eyes
            head.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(32,0).addBox(-2,-13,-2,4,5,4), PartPose.ZERO);
            head.addOrReplaceChild("left_sensor", CubeListBuilder.create().texOffs(0,32).addBox(-7,-4,-1,3,2,2), PartPose.ZERO);
            head.addOrReplaceChild("right_sensor", CubeListBuilder.create().texOffs(0,32).addBox(4,-4,-1,3,2,2), PartPose.ZERO);
        } else if (variant == 1) { // Cubed: blocky helmet
            head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(32,16).addBox(-5,-9,-5,10,5,10), PartPose.ZERO);
        } else if (variant == 2) { // Ashle: horns
            head.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(40,0).addBox(-5,-13,-1,2,5,2), PartPose.ZERO);
            head.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(40,0).addBox(3,-13,-1,2,5,2), PartPose.ZERO);
            root.addOrReplaceChild("left_shoulder", CubeListBuilder.create().texOffs(32,32).addBox(-3,-3,-3,6,6,6), PartPose.offset(-5,7,0));
            root.addOrReplaceChild("right_shoulder", CubeListBuilder.create().texOffs(32,32).addBox(-3,-3,-3,6,6,6), PartPose.offset(5,7,0));
        } else { // Slacker: hunched broad head
            head.addOrReplaceChild("brow", CubeListBuilder.create().texOffs(32,0).addBox(-5,-5,-5,10,3,2), PartPose.ZERO);
        }
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40,16).addBox(-1,-2,-2,4,12,4), PartPose.offset(5,6,0));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40,16).mirror().addBox(-3,-2,-2,4,12,4), PartPose.offset(-5,6,0));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0,16).addBox(-2,0,-2,4,12,4), PartPose.offset(2,12,0));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0,16).mirror().addBox(-2,0,-2,4,12,4), PartPose.offset(-2,12,0));
        return LayerDefinition.create(mesh, 64, 64);
    }
    @Override public ModelPart root() { return root; }
    @Override public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * ((float)Math.PI / 180F); head.xRot = headPitch * ((float)Math.PI / 180F);
        leftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        rightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        leftArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.2F * limbSwingAmount;
        rightArm.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;
    }
}
