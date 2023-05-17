import {Section} from "./Section";
import {Text, View} from "react-native";
import React, {useState} from "react";
import {CustomInput} from "./CustomInput";
import {Btn} from "./Btn";
import {isArrayFilledWithTrue, modifyElementInArrayByIndex} from "../utils/utils";

interface Quest {
    description: string;
    id: number;
    name: string;

}

interface Props {
    questsList: Quest[];
    GMMode?: boolean;
}



export function CharacterQuests({questsList, GMMode}: Props) {

    const [areInputsValid, setAreInputsValid] = useState<boolean[]>(Array(2));

    let questNameInputValue = '';
    let questDescriptionInputValue = '';

    function sendNewQuestRequest(){

    }

    const questElements = questsList.map((quest: Quest, index: number) => {
        return (
            <View className={'w-full'} key={index}>
                {
                    index !== 0 &&
                    <View className={'w-full bg-color-accent rounded h-1 my-3'}></View>
                }
                <Text className={'text-color-white text-xl font-bold'}>
                    {quest.name}
                </Text>
                <Text className={'text-color-white text-l'}>
                    {quest.description}
                </Text>
            </View>
        );
    })

    console.log(areInputsValid);

    return (
        <Section colorVariant={'dark'} title={'Quests'}>
            {
                GMMode &&
                <View className={'flex-col'}>
                    <CustomInput
                        placeholder={'Quest title'}
                        regex={/^[a-zA-Z,-]{4,30}$/}
                        func={(value: string, isValid: boolean) => {
                            console.log(value);
                            questNameInputValue = value;
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 0, isValid));

                        }}
                    />
                    <CustomInput
                        placeholder={'Quest description'}
                        regex={/^[a-zA-Z,-]{4,100}$/}
                        func={(value: string, isValid: boolean) => {
                            questDescriptionInputValue = value;
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 1, isValid));

                        }}
                    />
                    <Btn
                        func={() => {

                        }}
                        text={'Add quest'}
                        additionalTailwindClasses={'self-center'}
                        disabled={!isArrayFilledWithTrue(areInputsValid)}
                    />
                </View>


            }
            <View className={''}>
                {questElements}
            </View>
        </Section>
    );
}




