package io.github.winneonsword.mm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.server.v1_6_R2.BiomeBase;
import net.minecraft.server.v1_6_R2.BiomeMeta;
import net.minecraft.server.v1_6_R2.EntityInsentient;
import net.minecraft.server.v1_6_R2.EntitySkeleton;
import net.minecraft.server.v1_6_R2.EntityTypes;

import org.bukkit.entity.EntityType;

public enum CustomEntityType {
	
	SKELETON("Skeleton", 51, EntityType.SKELETON, EntitySkeleton.class, CustomEntitySkeleton.class);
	
	private String name;
	private int id;
	private EntityType entityType;
	private Class<? extends EntityInsentient> nmsClass;
	private Class<? extends EntityInsentient> customClass;
	
	private CustomEntityType(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass){
		this.name = name;
		this.id = id;
		this.entityType = entityType;
		this.nmsClass = nmsClass;
		this.customClass = customClass;
	}
	public String getName(){
		return this.name;
	}
	public int getID(){
		return this.id;
	}
	public EntityType getEntityType(){
		return this.entityType;
	}
	public Class<? extends EntityInsentient> getNMSClass(){
		return this.nmsClass;
	}
	public Class<? extends EntityInsentient> getCustomClass(){
		return this.customClass;
	}
	public static void registerEntities(){
		for (CustomEntityType entity : values()){
			try {
				Method a = EntityTypes.class.getDeclaredMethod("a", new Class<?>[]{ Class.class, String.class, int.class });
				a.setAccessible(true);
				a.invoke(null, entity.getCustomClass(), entity.getName(), entity.getID());
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		for (BiomeBase biomeBase : BiomeBase.biomes){
			if (biomeBase == null){
				break;
			}
			for (String field : new String[]{ "K", "J", "L", "M" }){
				try {
					Field list = BiomeBase.class.getDeclaredField(field);
					list.setAccessible(true);
					
					@SuppressWarnings("unchecked")
					List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
					for (BiomeMeta meta : mobList){
						for (CustomEntityType entity : values()){
							if (entity.getNMSClass().equals(meta.b)){
								meta.b = entity.getCustomClass();
							}
						}
					}
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}