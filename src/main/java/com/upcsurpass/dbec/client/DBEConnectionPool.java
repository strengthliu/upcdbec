package com.upcsurpass.dbec.client;

import java.util.Enumeration;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upcsurpass.dbec.service.method.GetServerCurrentTime;

public abstract class DBEConnectionPool {
	private static final Logger LOGGER = LoggerFactory.getLogger(DBEConnectionPool.class);
    public static int numObjects = 1; // 对象池的大小
    public static int maxObjects = 20; // 对象池最大的大小
    protected Vector<PooledNIOSocketClient> objects = null; // 存放对象池中对象的向量(PooledObject类型)

    public DBEConnectionPool() {
    }

    /*** 创建一个对象池 ***/
    public synchronized void createPool() {
        // 确保对象池没有创建。如果创建了，保存对象的向量 objects 不会为空
        if (objects != null) {
            return; // 如果己经创建，则返回
        }
        // 创建保存对象的向量 , 初始时有 0 个元素
         objects = new Vector<PooledNIOSocketClient>();
         for (int i = 0; i < numObjects; i++) {
                objects.addElement(create());
            }
    }

    public abstract PooledNIOSocketClient create();

    public synchronized PooledNIOSocketClient getObject() {
        // 确保对象池己被创建
        if (objects == null) {
            return null; // 对象池还没创建，则返回 null
        }
        PooledNIOSocketClient t = getFreeObject(); // 获得一个可用的对象
        // 如果目前没有可以使用的对象，即所有的对象都在使用中
        while (t == null) {
            wait(250);
            t = getFreeObject(); // 重新再试，直到获得可用的对象，如果
            // getFreeObject() 返回的为 null，则表明创建一批对象后也不可获得可用对象
        }
        return t;// 返回获得的可用的对象
    }

    /**
     * 本函数从对象池对象 objects 中返回一个可用的的对象，如果 当前没有可用的对象，则创建几个对象，并放入对象池中。
     * 如果创建后，所有的对象都在使用中，则返回 null
     */
    private PooledNIOSocketClient getFreeObject() {
        // 从对象池中获得一个可用的对象
    	PooledNIOSocketClient obj = findFreeObject();
        if (obj == null) {
            createObjects(10); // 如果目前对象池中没有可用的对象，创建一些对象
            // 重新从池中查找是否有可用对象
            obj = findFreeObject();
            // 如果创建对象后仍获得不到可用的对象，则返回 null
            if (obj == null) {
                return null;
            }
        }
        return obj;
    }

    public void createObjects(int increment){
        for (int i = 0; i < increment; i++) {
            if (objects.size() > maxObjects) {
                return;
            }
            objects.addElement(create());
        }
    }

    /**
     * 查找对象池中所有的对象，查找一个可用的对象， 如果没有可用的对象，返回 null
     */
    private PooledNIOSocketClient findFreeObject() {
    	PooledNIOSocketClient obj = null;
        PooledNIOSocketClient pObj = null;
        // 获得对象池向量中所有的对象
        Enumeration<PooledNIOSocketClient> enumerate = objects.elements();
        // 遍历所有的对象，看是否有可用的对象
        while (enumerate.hasMoreElements()) {
            pObj = (PooledNIOSocketClient) enumerate.nextElement();
            // 如果此对象不忙，则获得它的对象并把它设为忙
            if (!pObj.isBusy()) {
                obj = pObj;
                pObj.setBusy(true);
            }
        }
        return obj;// 返回找到到的可用对象
    }

    /**
     * 此函数返回一个对象到对象池中，并把此对象置为空闲。 所有使用对象池获得的对象均应在不使用此对象时返回它。
     */

    public void returnObject(PooledNIOSocketClient obj) {
        // 确保对象池存在，如果对象没有创建（不存在），直接返回
        if (objects == null) {
            return;
        }
        PooledNIOSocketClient pObj = null;
        Enumeration<PooledNIOSocketClient> enumerate = objects.elements();
        // 遍历对象池中的所有对象，找到这个要返回的对象对象
        while (enumerate.hasMoreElements()) {
            pObj = (PooledNIOSocketClient) enumerate.nextElement();
            // 先找到对象池中的要返回的对象对象
            if (obj == pObj) {
                // 找到了 , 设置此对象为空闲状态
                pObj.setBusy(false);
                break;
            }
        }
    }

    /**
     * 关闭对象池中所有的对象，并清空对象池。
     */
    public synchronized void closeObjectPool() {
        // 确保对象池存在，如果不存在，返回
        if (objects == null) {
            return;
        }
        PooledNIOSocketClient pObj = null;
        Enumeration<PooledNIOSocketClient> enumerate = objects.elements();
        int i = 1;
        while (enumerate.hasMoreElements()) {
            pObj = (PooledNIOSocketClient) enumerate.nextElement();
            // 如果忙，等 0.5 秒
            if (pObj.isBusy()) {
                wait(500); // 等
            }
            LOGGER.debug("关闭："+i+" / "+objects.size());
            i++;
            pObj.close();
            LOGGER.debug("关闭 - 关闭结束 ");
        }
        Enumeration<PooledNIOSocketClient> enumerate1 = objects.elements();
        objects.removeAllElements();
//        i = 1;
//        while (enumerate1.hasMoreElements()) {
//            pObj = (PooledNIOSocketClient) enumerate1.nextElement();
//            LOGGER.debug("移除："+i+" / "+objects.size());
//            i++;
//            // 从对象池向量中删除它
//            objects.removeElement(pObj);
//            LOGGER.debug("关闭 - 移除结束 ");
//        }
        // 置对象池为空
        objects = null;
    }

    /**
     * 使程序等待给定的毫秒数
     */
    private void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
        }
    }
}