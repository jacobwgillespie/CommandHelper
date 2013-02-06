/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.core.functions;

import com.laytonsmith.abstraction.MCItemMeta;
import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.MCLeatherArmorMeta;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.Exceptions.ExceptionType;

/**
 *
 * @author Layton
 */
public class ItemMeta {
	public static String docs(){
		return "These functions manipulate an item's meta data. The items are modified in a player's inventory.";
	}
	
	@api(environments={CommandHelperEnvironment.class})
	public static class get_itemmeta extends AbstractFunction {

		public ExceptionType[] thrown() {
			return new ExceptionType[]{ExceptionType.PlayerOfflineException, ExceptionType.CastException};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
			MCPlayer p = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
			MCItemStack is;
			Construct slot;
			if(args.length == 2){
				p = Static.GetPlayer(args[0], t);
				slot = args[1];
			} else {
				slot = args[0];
			}
			Static.AssertPlayerNonNull(p, t);
			if (slot instanceof CNull) {
				is = p.getItemInHand();
			} else {
				is = p.getItemAt(Static.getInt32(slot, t));
			}
			if (is == null) {
				throw new Exceptions.CastException("There is no item at slot " + slot, t);
			}
			return ObjectGenerator.GetGenerator().itemMeta(is, t);
		}

		public String getName() {
			return "get_itemmeta";
		}

		public Integer[] numArgs() {
			return new Integer[]{1, 2};
		}

		public String docs() {
			return "mixed {[player,] inventorySlot} Returns an associative array of known ItemMeta for the slot given, "
					+ "or null if there isn't any. All items can have a display(name) and/or lore, and more info will "
					+ "be available for the items that have it. For example, leather armor will have a color field.";
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
		
	}
	
	@api(environments={CommandHelperEnvironment.class})
	public static class set_itemmeta extends AbstractFunction {

		public ExceptionType[] thrown() {
			return new ExceptionType[]{ExceptionType.PlayerOfflineException, ExceptionType.FormatException, ExceptionType.CastException};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
			MCPlayer p = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
			Construct slot, meta;
			MCItemStack is;
			if(args.length == 3){
				p = Static.GetPlayer(args[0], t);
				slot = args[1];
				meta = args[2];
			} else {
				slot = args[0];
				meta = args[1];
			}
			Static.AssertPlayerNonNull(p, t);
			if (slot instanceof CNull) {
				is = p.getItemInHand();
			} else {
				is = p.getItemAt(Static.getInt32(slot, t));
			}
			if (is == null) {
				throw new Exceptions.CastException("There is no item at slot " + slot, t);
			}
			is.setItemMeta(ObjectGenerator.GetGenerator().itemMeta(meta, is.getTypeId(), t));
			return new CVoid(t);
		}

		public String getName() {
			return "set_itemmeta";
		}

		public Integer[] numArgs() {
			return new Integer[]{2, 3};
		}

		public String docs() {
			return "void {[player,] inventorySlot, ItemMetaArray} Applies the data from the given array to the item at the "
					+ "specified slot. Unused fields will be ignored. If null or an empty array is supplied, or if none of "
					+ "the given fields are applicable, the item will become default, as this function overwrites any "
					+ "existing data. The array should be in the form array('display': 'Sword of Pointy-ness', 'lore': "
					+ "array('Look at my sword', 'my sword is amazing!')). Items with other meta can have additional "
					+ "fields, for example leather armor could have \"'color': array(r: 64, b: 106, g: 160)\" as a field.";
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
		
	}
	
	@api(environments={CommandHelperEnvironment.class})
	public static class get_armor_color extends AbstractFunction{

		public ExceptionType[] thrown() {
			return new ExceptionType[]{ExceptionType.CastException, ExceptionType.PlayerOfflineException};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
			MCPlayer p = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
			int slot;
			if(args.length == 2){
				p = Static.GetPlayer(args[0], t);
				slot = Static.getInt32(args[1], t);
			} else {
				slot = Static.getInt32(args[0], t);
			}
			Static.AssertPlayerNonNull(p, t);
			MCItemStack is = p.getItemAt(slot);
			if (is == null) {
				throw new Exceptions.CastException("There is no item at slot " + slot, t);
			}
			MCItemMeta im = is.getItemMeta();
			if(im instanceof MCLeatherArmorMeta){
				return ObjectGenerator.GetGenerator().color(((MCLeatherArmorMeta)im).getColor(), t);
			} else {
				throw new Exceptions.CastException("The item at slot " + slot + " is not leather armor.", t);
			}
		}

		public String getName() {
			return "get_armor_color";
		}

		public Integer[] numArgs() {
			return new Integer[]{1, 2};
		}

		public String docs() {
			return "colorArray {[player], inventorySlot} Returns a color array for the color of the leather armor at"
					+ " the given slot. A CastException is thrown if this is not leather armor at that slot. The color"
					+ " array returned will look like: array(r: 0, g: 0, b: 0)";
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
		
	}
	
	@api(environments={CommandHelperEnvironment.class})
	public static class set_armor_color extends AbstractFunction{

		public ExceptionType[] thrown() {
			return new ExceptionType[]{ExceptionType.CastException, ExceptionType.PlayerOfflineException, ExceptionType.FormatException};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
			MCPlayer p = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
			int slot;
			CArray color;
			if(args.length == 3){
				p = Static.GetPlayer(args[0], t);
				slot = Static.getInt32(args[1], t);
				if (args[2] instanceof CArray) {
					color = (CArray)args[2];
				} else {
					throw new Exceptions.FormatException("Expected an array but recieved " + args[2] + " instead.", t);
				}
			} else {
				slot = Static.getInt32(args[0], t);
				if (args[1] instanceof CArray) {
					color = (CArray)args[1];
				} else {
					throw new Exceptions.FormatException("Expected an array but recieved " + args[1] + " instead.", t);
				}
			}
			Static.AssertPlayerNonNull(p, t);
			MCItemStack is = p.getItemAt(slot);
			if (is == null) {
				throw new Exceptions.CastException("There is no item at slot " + slot, t);
			}
			MCItemMeta im = is.getItemMeta();
			if(im instanceof MCLeatherArmorMeta){
				((MCLeatherArmorMeta)im).setColor(ObjectGenerator.GetGenerator().color(color, t));
				is.setItemMeta(im);
			} else {
				throw new Exceptions.CastException("The item at slot " + slot + " is not leather armor", t);
			}
			return new CVoid(t);
		}

		public String getName() {
			return "set_armor_color";
		}

		public Integer[] numArgs() {
			return new Integer[]{2, 3};
		}

		public String docs() {
			return "void {[player], slot, colorArray} Sets the color of the leather armor at the given slot. colorArray"
					+ " should be an array that matches one of the formats: array(r: 0, g: 0, b: 0)"
					+ " array(red: 0, green: 0, blue: 0)"
					+ " array(0, 0, 0)";
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
		
	}
	
	@api(environments={CommandHelperEnvironment.class})
	public static class is_leather_armor extends AbstractFunction{

		public ExceptionType[] thrown() {
			return new ExceptionType[]{ExceptionType.CastException, ExceptionType.PlayerOfflineException};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
			MCPlayer p = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
			int slot;
			if(args.length == 2){
				p = Static.GetPlayer(args[0], t);
				slot = Static.getInt32(args[1], t);
			} else {
				slot = Static.getInt32(args[0], t);
			}
			Static.AssertPlayerNonNull(p, t);
			MCItemMeta im = p.getItemAt(slot).getItemMeta();
			return new CBoolean(im instanceof MCLeatherArmorMeta, t);
		}

		public String getName() {
			return "is_leather_armor";
		}

		public Integer[] numArgs() {
			return new Integer[]{1, 2};
		}

		public String docs() {
			return "boolean {[player], itemSlot} Returns true if the item at the given slot is a piece of leather"
					+ " armor, that is, if dying it is allowed.";
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
		
	}
}
