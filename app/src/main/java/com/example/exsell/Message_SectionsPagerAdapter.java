package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class   Message_SectionsPagerAdapter  extends FragmentPagerAdapter {
    public Message_SectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                message_chats_fragment message = new message_chats_fragment();

                return message;

            case 1:
                message_users_fragment users = new message_users_fragment();
                return users;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){

            case 0:
                return "CHAT";

            case 1:
                return "USERS";

                default:
                    return null;
        }

    }
}
