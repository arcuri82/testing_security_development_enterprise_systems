import React from "react";


const Card = (props) =>{

    let label = props.name;
    
    let imageClasses = "card-image";
    
    if(props.quantity){
        label += " ("+props.quantity+")"
    } else {
        imageClasses += " disabled"
    }

    let divClasses = "card " + props.rarity; 

    return(
        <div className={divClasses}>
            <h3 className="card-label">{label}</h3>
            <img className={imageClasses} src={"/api/cards/imgs/"+props.imageId}/>
        </div>
    );
};

export default Card;