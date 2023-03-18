import React, {useState} from "react";
import {Text, TouchableOpacity, View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {Section} from "../Components/Section";
import {Btn} from "../Components/Btn";

export function LoginScreen({navigation}: { navigation: any }) {

    const [mail, setMail] = useState('');
    console.log(mail);

    return (
        <View className={"flex-col justify-center h-full"}>
            <Section variant={"light"}>
                <View className={'items-center px-4'}>
                    <CustomInput
                        placeholder={"mail"}
                        func={()=>{

                        }}
                    />

                    <CustomInput
                        placeholder={"password"}
                        func={() => {
                        }}
                        password
                    />
                    <Btn
                        text={"Log in"}
                        func={()=>{
                            navigation.navigate('characterSheet');
                        }}
                    />
                    <Btn
                        text={"Sign in"}
                        func={()=>{
                            navigation.navigate('signIn');
                        }}
                    />
                </View>


            </Section>
        </View>

    );
}
