package org.chzzk.howmeet.domain.common.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Nicknames {

    private final List<Nickname> nicknames = new ArrayList<>();

    public static Nicknames create() {
        return new Nicknames();
    }

    public int size() {
        return nicknames.size();
    }

    public void add(final Nickname nickName) {
        this.nicknames.add(nickName);
    }

    public boolean contains(Nickname nickname){
        return nicknames.contains(nickname);
    }

    public void addAll(final Set<Nickname> nickNameSet) {
        this.nicknames.addAll(nickNameSet);
    }

//    public List<Nickname> getNicknames(){
//        return List.copyOf(nicknames);
//    }

    public static Nicknames convertNicknameProvidersList(final List<? extends NicknameProvider> nicknameProviders) {
        Nicknames nicknames = Nicknames.create();

        for (NicknameProvider provider : nicknameProviders) {
            nicknames.add(provider.getNickname());
        }
        return nicknames;
    }

    @JsonValue
    public List<String> toJson() {
        return List.copyOf(nicknames).stream()
                .map(Nickname::getValue) // 각 Nickname 객체의 value 값을 추출
                .collect(Collectors.toList());
    }
}