export function modifyElementInArrayByIndex(array: Array<any>, index:number, value:any):Array<any>{ //todo: test it
    array[index] = value;
    return array;
}

export function isArrayFilledWithTrue(array: Array<boolean>): boolean{
    for(const element of array){
        if(!element)
            return false;
    }
    return true;
}


