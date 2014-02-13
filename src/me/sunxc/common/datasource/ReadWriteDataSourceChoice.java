package me.sunxc.common.datasource;


/**
 * <pre>
 * 读/写动态数据库选择，通过本地线程进行标记，如果在一个事务当中先写后读则从写库读取
 * 根据DataSourceType是write/read 来决定是使用读/写数据库
 * 通过ThreadLocal绑定实现选择功能
 * </pre>
 * @author xianchao.sun@yunyoyo.cn
 *
 */
public class ReadWriteDataSourceChoice {
    
    public enum DataSourceType {
        write, read;
    }
    
    
    private static final ThreadLocal<DataSourceType> holder = new ThreadLocal<DataSourceType>();

    public static void markWrite() {
        holder.set(DataSourceType.write);
    }
    
    public static void markRead() {
        holder.set(DataSourceType.read);
    }
    
    public static void reset() {
        holder.set(null);
    }
    
    public static boolean isChoiceNone() {
        return null == holder.get(); 
    }
    
    public static boolean isChoiceWrite() {
        return DataSourceType.write == holder.get();
    }
    
    public static boolean isChoiceRead() {
        return DataSourceType.read == holder.get();
    }

}
