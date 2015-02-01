package com.lulan.shincolle.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.List;

import com.lulan.shincolle.network.createPacketS2C;
import com.lulan.shincolle.utility.LogHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**ENTITY ABYSS MISSILE
 * @parm world, host entity, tarX, tarY, tarZ, damage, knockback value
 * 
 * Parabola Orbit(for distance 7~65)
 * �Ϊ��]�w:
 * �b�g�L�Z�����I���e, �[�W�B�~motionY�V�W�H��accY�V�U
 * �줤�I��, Vy = 0
 * 
 */
public class EntityAbyssMissile extends Entity {
    public EntityLivingBase hostEntity;  //host target
    public Entity hitEntity;			 //onImpact target (for entity)
    
    //missile motion
    public double distX;			//target distance
    public double distY;
    public double distZ;
    public boolean isDirect;		//false:parabola  true:direct
  
    //for parabola y position
    public double accParaY;			//�B�~y�b�[�t��
    public int midFlyTime;			//�@�b������ɶ�
    
    //for direct only
    public static final double ACCE = 0.02D;		//�w�]�[�t��
    public double accX;				//�T�b�[�t��
    public double accY;
    public double accZ;
    
    //missile attributes
    public float atk;				//missile damage
    public float kbValue;			//knockback value
    public float missileHP;			//if hp = 0 -> onImpact
    public boolean isTargetHurt;	//knockback flag
    public World world;

    
    public EntityAbyssMissile(World world) {
    	super(world);
    }
    
    public EntityAbyssMissile(World world, EntityLivingBase host, double tarX, double tarY, double tarZ, float atk, float kbValue, boolean isDirect) {
        super(world);
        this.world = world;
        //�]�wentity���o�g��, �Ω�l�ܳy���ˮ`���ӷ�
        this.hostEntity = host;
        this.setSize(1.0F, 1.0F);
        this.atk = atk;
        this.kbValue  = kbValue;
        //�]�w�o�g��m (posY�|�[�Woffset), ���k+�W�U����, �H��
        this.posX = host.posX;
        this.posY = host.posY+host.height*0.5D;
        this.posZ = host.posZ;     
        //�p��Z��, ���o��Vvector, �åB��l�Ƴt��, �ϭ��u��V�¦V�ؼ�
        this.distX = tarX - this.posX;
        this.distY = tarY - this.posY;
        this.distZ = tarZ - this.posZ;
        //�]�w���g�Ϊ̩ߪ��u
        this.isDirect = isDirect;
        
        //���g�u�D, no gravity
    	double dist = (double)MathHelper.sqrt_double(this.distX*this.distX + this.distY*this.distY + this.distZ*this.distZ);
  	    this.accX = this.distX / dist * this.ACCE;
	    this.accY = this.distY / dist * this.ACCE;
	    this.accZ = this.distZ / dist * this.ACCE;
	    this.motionX = this.accX;
	    this.motionZ = this.accY;
	    this.motionY = this.accZ;
 
	    //�ߪ��u�y�D�p��, y�b��t�[�W (�@�b����ɶ� * �B�~y�b�[�t��)
	    if(!this.isDirect) {
	    	this.midFlyTime = (int) (0.5D * MathHelper.sqrt_double(2D * dist / this.ACCE));
	    	this.accParaY = this.ACCE;
	    	this.motionY = this.motionY + (double)this.midFlyTime * this.accParaY;
	    }
    }

    protected void entityInit() {}

    /**
     * Checks if the entity is in range to render by using the past in distance and 
     * comparing it to its average bounding box edge length * 64 * renderDistanceWeight 
     * Args: distance
     * 
     * �ѩ�entity�i�ण��������, �G����������j�p�ӭp��Z��, ����k�w�]��256������j�p
     */
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distanceSq) {
        double d1 = this.boundingBox.getAverageEdgeLength() * 256D;
        return distanceSq < d1 * d1;
    }

    //update entity
    //�`�N: ���ʭn�bserver+client�����e���~����ܥ���, particle�h�u��bclient��
    public void onUpdate() {    	
    	/**********both side***********/
    	//�N��m��s (�]�tserver, client���P�B��m, �~���bounding box�B�@���`)
        this.setPosition(this.posX, this.posY, this.posZ);
 
    	//�p��o�g�骺����
    	if(!this.isDirect) {  //���g�y�D�p��  	
			this.motionY = this.motionY + this.accY - this.accParaY;                   
    	}
    	else {
    		this.motionY += this.accY;
    	}
    	
    	//�p��next tick���t��
        this.motionX += this.accX;
        this.motionZ += this.accZ;
        
    	//�]�w�o�g�骺�U�@�Ӧ�m
		this.posX += this.motionX;
		this.posY += this.motionY;
        this.posZ += this.motionZ;
           	
    	//�p��ҫ��n�઺���� (RAD, not DEG)
        float f1 = MathHelper.sqrt_double(this.motionX*this.motionX + this.motionZ*this.motionZ);
        this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f1));
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ));    
        
        //�̷�x,z�b���t�V�ץ�����(��180)
        if(this.distX > 0) {
        	this.rotationYaw -= Math.PI;
        }
        else {
        	this.rotationYaw += Math.PI;
        }
        
        //��s��m�����򥻸�T, �P�ɧ�sprePosXYZ
        super.onUpdate();
        
        /**********server side***********/
    	if(!this.worldObj.isRemote) {	
    		//�o�g�W�L20 sec, �]�w�����`(����), �`�Nserver restart�ᦹ�ȷ|�k�s
    		if(this.ticksExisted > 600) {
    			this.setDead();	//�����ٮ�, ��Ĳ�o�z��
    		}
    		
    		//�Ӧ�m�I����, �h�]�w�z�� (��k1: �����ήy�Ч���) ����k�ѩ��y�Ш�int, �ܦh�ɭԬݰ_�Ӧ�������O�̵M�줣����
    		if(!this.worldObj.blockExists((int)this.posX, (int)this.posY, (int)this.posZ)) {
    			this.onImpact(null);
    		}
    		
    		//�Ӧ�m�I����, �h�]�w�z�� (��k2: ��raytrace����)
    		Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);          
            vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null) {
                vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
                this.onImpact(null);
            }
    		
//            //debug
//            if(this.hostEntity != null) {
//            	LogHelper.info("DEBUG : tick "+this.ticksExisted);
//            	LogHelper.info("DEBUG : motionY "+this.motionY);
//            	LogHelper.info("DEBUG : mieeile rot: "+this.rotationPitch*180/Math.PI+" "+this.rotationYaw*180/Math.PI);
//            	LogHelper.info("DEBUG : host rot: "+this.hostEntity.rotationPitch+" "+this.hostEntity.rotationYaw);
//            	LogHelper.info("DEBUG : missile mot: "+this.motionX+" "+this.motionY+" "+this.motionZ);
//        		LogHelper.info("DEBUG : host pos: "+this.hostEntity.posX+" "+this.hostEntity.posY+" "+this.hostEntity.posZ); 
//            	LogHelper.info("DEBUG : tar pos: "+this.targetX+" "+this.targetY+" "+this.targetZ);
//            	LogHelper.info("DEBUG : diff pos: "+this.distX+" "+this.distY+" "+this.distZ);
//            	LogHelper.info("DEBUG : AABB: "+this.boundingBox.toString());
//            }
            
            //�P�wbounding box���O�_���i�HĲ�o�z����entity
            hitEntity = null; 
            List hitList = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox);
            //�j�Mlist, ��X�Ĥ@�ӥi�H�P�w���ؼ�, �Y�ǵ�onImpact
            if(hitList != null && !hitList.isEmpty()) {
                for(int i=0; i<hitList.size(); ++i) { 
                	hitEntity = (Entity)hitList.get(i);
                	if(hitEntity.canBeCollidedWith() && (!hitEntity.isEntityEqual(this.hostEntity) || this.ticksExisted > 30)) {               		
                		break;	//break for loop
                	}
                	else {
                		hitEntity = null;
                	}
                }
            }
            //call onImpact
            if(hitEntity != null) {
            	this.onImpact((EntityLivingBase)hitEntity);
            } 
            
    	}//end server side
    	/**********client side***********/
    	else {
    		//spawn particle
            for (int j = 0; j < 6; ++j) {
                this.worldObj.spawnParticle("cloud", this.posX-this.motionX*1.5D*j, this.posY+1D-this.motionY*1.5D*j, this.posZ-this.motionZ*1.5D*j, -this.motionX*0.5D, -this.motionY*0.5D, -this.motionZ*0.5D);
            }
    	}//end client side
    	   	
    }

    //�����P�w�ɩI�s����k
    protected void onImpact(EntityLivingBase entityHit) {
    	//server side
    	if(!this.worldObj.isRemote) {  		
            if(entityHit != null) {	//����entity�ް_�z��
            	//�]�w��entity���쪺�ˮ`
            	isTargetHurt = entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.hostEntity), this.atk);

        	    //if attack success
        	    if(isTargetHurt) {
        	    	//calc kb effect
        	        if(this.kbValue > 0) {
        	        	entityHit.addVelocity((double)(-MathHelper.sin(rotationYaw * (float)Math.PI / 180.0F) * kbValue), 
        	                   0.1D, (double)(MathHelper.cos(rotationYaw * (float)Math.PI / 180.0F) * kbValue));
        	            motionX *= 0.6D;
        	            motionZ *= 0.6D;
        	        }             	 
        	    }
            }
            else {	//����block�Ψ�L��]�ް_�z��, �h�b���u�̫�a�I���z
            	//�P�wbounding box���O�_���i�H�Y�ˮ`��entity
                hitEntity = null;
                AxisAlignedBB impactBox = this.boundingBox.expand(3D, 3D, 3D); 
                List hitList = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, impactBox);
                //�j�Mlist, ��X�Ĥ@�ӥi�H�P�w���ؼ�, �Y�ǵ�onImpact
                if(hitList != null && !hitList.isEmpty()) {
                    for(int i=0; i<hitList.size(); ++i) { 
                    	hitEntity = (Entity)hitList.get(i);
                    	if(hitEntity.canBeCollidedWith() && (!hitEntity.isEntityEqual(this.hostEntity) || this.ticksExisted > 20)) {               		
                    		//��entity�y���ˮ`
                    		isTargetHurt = hitEntity.attackEntityFrom(DamageSource.causeMobDamage(this.hostEntity), this.atk);
                    	    //if attack success
                    	    if(isTargetHurt) {
                    	    	//calc kb effect
                    	        if(this.kbValue > 0) {
                    	        	hitEntity.addVelocity((double)(-MathHelper.sin(rotationYaw * (float)Math.PI / 180.0F) * kbValue), 
                    	                   0.1D, (double)(MathHelper.cos(rotationYaw * (float)Math.PI / 180.0F) * kbValue));
                    	            motionX *= 0.6D;
                    	            motionZ *= 0.6D;
                    	        }             	 
                    	    }
                    	}
                    }
                }          	
            }
            
            //send packet to client for display partical effect 
            createPacketS2C.sendS2CAttackParticle(this, 2);
            this.setDead();
        }//end if server side
    }

    //�x�sentity��nbt
    public void writeEntityToNBT(NBTTagCompound nbt) {
    	nbt.setTag("direction", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));  
    	nbt.setFloat("atk", this.atk);
    }

    //Ū��entity��nbt
    public void readEntityFromNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("direction", 9)) {	//9��tag list
            NBTTagList nbttaglist = nbt.getTagList("direction", 6);	//6��tag double
            this.motionX = nbttaglist.func_150309_d(0);	//����get double
            this.motionY = nbttaglist.func_150309_d(1);
            this.motionZ = nbttaglist.func_150309_d(2);
        }
        else {
            this.setDead();
        }
        
        this.atk = nbt.getFloat("atk");
    }

    //�]�wtrue�i�Ϩ�L�ͪ��P�w�O�_�n�{�}��entity
    public boolean canBeCollidedWith() {
        return true;
    }

    //���o��entity��bounding box�j�p
    public float getCollisionBorderSize() {
        return 1.0F;
    }

    //entity�Q������ɩI�s����k
    public boolean attackEntityFrom(DamageSource attacker, float atk) {
        if(this.isEntityInvulnerable()) {	//��L�ĥؼЦ^��false
            return false;
        }
        
        this.onImpact(null);
        return true;
    }

    //render��, ���v�j�p
    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    //�p����u��
    public float getBrightness(float p_70013_1_) {
        return 1.0F;
    }

    //render��, �G�׭��ݩ�G����
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float p_70070_1_) {
        return 15728880;
    }
}