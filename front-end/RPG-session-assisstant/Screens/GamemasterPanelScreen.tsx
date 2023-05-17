import React, {useEffect} from "react";
import {ScrollView, Text, TouchableOpacity, View} from "react-native";
import {CharacterData} from "../utils/CharacterData";
import {GETRequestWithAuthKey, getUserDataFromLocalStorage} from "../utils/utils";
import {API_URL} from "../env";
import {CharacterInfo} from "../Components/CharacterInfo";
import {Section} from "../Components/Section";
import {CharacterAttributes} from "../Components/CharacterAttributes";
import {CharacterQuests} from "../Components/CharacterQuests";
import {CharacterInventory} from "../Components/CharacterInventory";

export function GamemasterPanelScreen({route, navigation}: any) {

    const [charactersList, setCharacterList] = React.useState([]);
    const [isCharacterListVisible, setIsCharacterListVisible] = React.useState(false);

    const [currentCharacterData, setCurrentCharacterData] = React.useState<CharacterData>();
    const [currentCharacterId, setCurrentCharacterId] = React.useState<number>(0);

    let authKey: string | null;
    let playerId: string | null;

    async function getCharacterList() {
        try {
            const playerData = await getUserDataFromLocalStorage()
            authKey = playerData.authKey;
            playerId = playerData.playerId;

            const getCharactersDataUrl = API_URL + '/api/room/' + route.params.roomId + '/characters';
            const res = await GETRequestWithAuthKey(getCharactersDataUrl, authKey)

            setCharacterList(res.data);
            setCurrentCharacterData(res.data[currentCharacterId]);//default character is the first character

        } catch (error) {
            console.log('get characters list request error: ' + error);
        }
    }

    function changeCurrentCharacter(characterId: number){
        setCurrentCharacterData(charactersList[characterId]);
        setCurrentCharacterId(characterId);
    }


    useEffect(() => {
        getCharacterList()
            .catch(error => console.error(error));
    }, [])

    function characterListElement() {
        if (charactersList === undefined) {
            return;
        }


        const characterLabelsElemets = charactersList.map((character: any, index:number) => {
            return (
                <TouchableOpacity
                    onPress={()=>{
                        changeCurrentCharacter(index);
                    }}
                >
                    <Section colorVariant={'dark'} key={character.id}>
                        <View className={'flex-row justify-between'}>
                            <Text className={'text-xl text-color-white'}>
                                {character.name}
                            </Text>
                            <Text className={'text-xl text-color-accent'}>
                                {character.level}lvl
                            </Text>
                        </View>
                    </Section>
                </TouchableOpacity>

            )
        })

        return (
            <Section colorVariant={'light'} title={'Select character'}>
                {characterLabelsElemets}
            </Section>
        );
    }


    if (currentCharacterData === undefined) {
        return <></>
    }

    return (
        <ScrollView>

            <CharacterInfo
                name={currentCharacterData.name}
                description={currentCharacterData.description}
                level={currentCharacterData.level}
                experience={currentCharacterData.experience}
                GMMode={true}
                showCharacterListFunc={() => {
                    setIsCharacterListVisible(prevState => !prevState);
                }}
            />
            {
                isCharacterListVisible &&
                characterListElement()
            }

            <CharacterAttributes
                attributesList={currentCharacterData.characterAttributes}
                freeSkillPoints={currentCharacterData.skillPoints}
                refreshFunc={()=>{}}
            />
            <CharacterQuests
                questsList={currentCharacterData.quests}
                GMMode={true}
            />
            <CharacterInventory
                itemsList={currentCharacterData.items}
            />


        </ScrollView>
    );
}
