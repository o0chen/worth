package com.blackeye.worth.tree;

import com.blackeye.worth.model.SysMenuPermission;
import com.blackeye.worth.utils.StringX;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 树工具类
 */
public class TreeUtils {

    /**
     * 递归建树
     * @param ts
     * @param <T>
     * @return
     */
    public static <T extends Tree> List<T> buildTree(List<T> ts) {
        List<T> trees = new ArrayList<>();
        // 获取所有根节点并递归遍历找到它们的儿子们
        ts.stream().forEach(t -> {
            if (StringX.isEmpty(t.getParentId())) {
                trees.add(findChildren((T) t, ts));
            }
        });
        return trees;
    }

    /**
     * 递归查找子节点
     * @param tree
     * @param trees
     * @return
     */
    private static <T extends Tree> T findChildren(T tree, List<T> trees) {
        trees.stream().forEach(t -> {
            if (!StringX.isEmpty(t.getParentId()) && t.getParentId().equals(tree.getId())) {
                if (CollectionUtils.isEmpty(tree.getChildren())) {
                    tree.setChildren(new LinkedHashSet<>());
                }
                tree.getChildren().add(findChildren((T) t, trees));
            }
        });
        return tree;
    }

    /**
     * 根据菜单列表建立菜单树
     * @param menus
     * @return
     */
    public static List<MenuTree> buildMenuTree(List<SysMenuPermission> menus) {
        List<MenuTree> menuTrees = new ArrayList<>();
        menus.stream().forEach(menu -> menuTrees.add(new MenuTree(menu)));
        List<MenuTree> mts = TreeUtils.buildTree(menuTrees);
        return mts;
    }
}
