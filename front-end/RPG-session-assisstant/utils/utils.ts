import axios, {Axios, AxiosResponse} from "axios";
import AsyncStorage from "@react-native-async-storage/async-storage";

export function modifyElementInArrayByIndex(array: Array<any>, index: number, value: any): Array<any> { //todo: test it
    array[index] = value;
    return array;
}

export function isArrayFilledWithTrue(array: Array<boolean>): boolean {
    for (const element of array) {
        if (!element)
            return false;
    }
    return true;
}

export function sendRequest(url: string, data: any, method: string = 'POST', headers: any = {}) {
    const axiosMethod = method === 'POST' ? axios.post : axios.get;
    return axiosMethod(url, data)
        .then(res => res)
        .catch(err => {
            console.log(err);
            return err;
        })
}

interface StorageItem {
    key: string;
    value: any;
}

export async function getUserDataFromLocalStorage() {
    const authKey = await AsyncStorage.getItem('@loginAuthKey');
    const playerId = await AsyncStorage.getItem('@loginUserId');

    return {authKey, playerId};
}

export async function GETRequestWithAuthKey(url: string, key: string | null) {
    return await axios.get(url, {
        headers: {
            Authorization: key,
        }
    })
}

export async function requestWithAuthKey(
    url: string,
    key: string | null,
    method: "POST" | "GET" | "PATCH",
    body?: any) :Promise<AxiosResponse>{

    if (method === "GET") {
        return await axios.get(url, {
            headers: {
                Authorization: key,
            }
        })
    }

    if (method === "POST") {
        return await axios.post(
            url,
            body,
            {
                headers: {
                    Authorization: key,
                }
            })
    }

    if (method === "PATCH") {
        return await axios.patch(
            url,
            body,
            {
                headers: {
                    Authorization: key,
                }
            })
    }

    return Promise.reject();

}

export function saveToAsyncStorage(items: Array<StorageItem>): Promise<any> {
    let promiseRejection;

    for (const item of items) {
        AsyncStorage.setItem(item.key, item.value)
            .catch(err => {
                console.log(err);
                promiseRejection = err;
            })
    }
    return promiseRejection ?
        Promise.reject(promiseRejection) : Promise.resolve();
}


