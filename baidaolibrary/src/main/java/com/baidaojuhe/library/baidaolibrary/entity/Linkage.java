/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.entity;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by box on 2017/4/19.
 * <p>
 * n级联动
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class Linkage {

    private String name;
    private List<Linkage> linkages;
    private Object tag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Linkage> getLinkages() {
        return linkages;
    }

    public void setLinkages(List<Linkage> linkages) {
        this.linkages = linkages;
    }

    public void addLinkages(List<Linkage> linkages) {
        if (this.linkages == null) {
            this.linkages = new ArrayList<>();
        }
        this.linkages.addAll(linkages);
    }

    public void addLinkage(int index, Linkage linkage) {
        if (linkages == null) {
            linkages = new ArrayList<>();
        }
        linkages.add(index, linkage);
    }

    @SuppressWarnings("unchecked")
    public <T> T getTag() {
        return (T) tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Linkage create(@NonNull String name, Object tag) {
        Linkage linkage = create(name, null);
        linkage.setTag(tag);
        return linkage;
    }

    public static Linkage create(@NonNull String name) {
        return create(name, null);
    }

    public static Linkage create(@NonNull String name, List<Linkage> linkages) {
        Linkage linkage = new Linkage();
        linkage.setName(name);
        linkage.setLinkages(linkages);
        return linkage;
    }
}
