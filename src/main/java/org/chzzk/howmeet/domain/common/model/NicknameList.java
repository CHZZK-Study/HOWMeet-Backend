package org.chzzk.howmeet.domain.common.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.entity.UserDetails;
import org.chzzk.howmeet.domain.temporary.auth.entity.Guest;
import org.chzzk.howmeet.domain.temporary.record.entity.GuestScheduleRecord;

@NoArgsConstructor
public class NicknameList {
    private final List<String> nicknames = new ArrayList<>();

    @JsonValue
    public List<String> getNicknames() {
        return nicknames;
    }

    public int size(){
        return getNicknames().size();
    }

    public void add(Nickname nickName){
        this.nicknames.add(nickName.getValue());
    }

    public void addFromNicknameSet(HashSet<Nickname> nickNameSet) {
        this.nicknames.addAll(nickNameSet.stream()
                .map(Nickname::getValue)
                .collect(Collectors.toList()));
    }

    public static NicknameList convertNicknameProvidersList(List<? extends NicknameProvider> nicknameProviders){
        NicknameList nicknameList = new NicknameList();

        for (NicknameProvider provider : nicknameProviders) {
            nicknameList.add(provider.getNickname());
        }
        return nicknameList;
    }
}