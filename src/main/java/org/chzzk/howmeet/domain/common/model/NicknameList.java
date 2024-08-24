package org.chzzk.howmeet.domain.common.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NicknameList {

    private final List<String> nicknames = new ArrayList<>();

    @JsonValue
    public List<String> getNicknames() {
        return nicknames;
    }

    public int size() {
        return getNicknames().size();
    }

    public void add(final Nickname nickName) {
        this.nicknames.add(nickName.getValue());
    }

    public void addFromNicknameSet(final HashSet<Nickname> nickNameSet) {
        this.nicknames.addAll(nickNameSet.stream()
                .map(Nickname::getValue)
                .collect(Collectors.toList()));
    }

    public static NicknameList convertNicknameProvidersList(final List<? extends NicknameProvider> nicknameProviders) {
        NicknameList nicknameList = new NicknameList();

        for (NicknameProvider provider : nicknameProviders) {
            nicknameList.add(provider.getNickname());
        }
        return nicknameList;
    }
}