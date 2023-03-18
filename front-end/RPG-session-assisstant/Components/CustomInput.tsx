import {TextInput, View, Text} from "react-native";


interface Props {
    placeholder: string;
    func: () => void;
    password?: boolean;
    regex?: string;
    variant?: "light" | "dark";
}

export function CustomInput({placeholder, func, password = false, regex = "[\\s\\S]*", variant = "light"}: Props) {
    const tailwind = require("../tailwind.config");
    return (


        <TextInput
            className={"bg-color-white rounded-xl m-2 px-3 py-2 text-color-greyLight font-bold text-xl w-full"}
            style={{
                shadowColor: "#000000",
                shadowOffset: {
                    width: 0,
                    height: 14,
                },
                shadowOpacity:  0.24,
                shadowRadius: 15.38,
                elevation: 19
            }}
            placeholder={placeholder}
            placeholderTextColor={tailwind.theme.extend.colors.color["greyLight"]}
            onChange={func}
            secureTextEntry={password}
        />
        // <View style={{shadowColor: '#000',shadowOffset: {width: 0, height: 2},shadowOpacity: 0.8,shadowRadius: 2,elevation: 10}}>
        //     <View style={{backgroundColor: 'white', padding: 10}}>
        //         <Text>Inner View</Text>
        //     </View>
        // </View>


    );
}
