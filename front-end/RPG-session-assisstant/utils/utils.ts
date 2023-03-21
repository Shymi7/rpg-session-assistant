export function modifyElementInArrayByIndex(array: Array<any>, index:number, value:any):Array<any>{ //todo: test it
    array[index] = value;
    return array;
}
