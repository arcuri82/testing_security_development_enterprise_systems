import React from "react";


const Card = (props) =>{

    let label = props.name;
    if(props.quantity){
        label += " ("+props.quantity+")"
    }

    return(
        <div>
            <p>{label}</p>
            <img src={"/api/cards/imgs/"+props.imageId}/>
        </div>
    );
};

export default Card;