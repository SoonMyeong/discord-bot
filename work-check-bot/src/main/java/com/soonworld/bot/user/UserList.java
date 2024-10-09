package com.soonworld.bot.user;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserList {
    private final List<String> completedUserList;

    public UserList() {
        this.completedUserList = new ArrayList<>();
    }

    public void add(String user) {
        completedUserList.add(user);
    }

    public boolean contains(String user) {
        return completedUserList.contains(user);
    }

    public boolean isFull() {
        return completedUserList.size() == 5;
    }

    public void clear() {
        completedUserList.clear();
    }
}
