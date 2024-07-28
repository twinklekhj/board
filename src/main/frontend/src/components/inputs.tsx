import React from 'react';

export interface InputGroupProps {
    id: string;
    name: string;
    icon?: React.ReactNode;
    placeholder?: string;
    value?: string;
    type?: 'text' | 'number' | 'password';
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
    buttonUse?: boolean;
    buttonName?: string;
    buttonClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
}

const InputGroup = (props: InputGroupProps) => {
    return (
        <div className="input-group">
            <label htmlFor={props.id}>{props.icon} {props.name}</label>
            <input
                id={props.id}
                name={props.id}
                type={props.type}
                value={props.value}
                placeholder={props.placeholder}
                onChange={props.onChange}
                className={"form-control"}
            />
            {props.buttonUse && (
                <button onClick={props.buttonClick} className="btn btn-default">
                    {props.buttonName || "제출"}
                </button>
            )}
        </div>
    );
};

export {InputGroup}
