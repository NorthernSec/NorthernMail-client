package com.NorthernSec.NorthernMail.Objects.Mail;

import com.NorthernSec.NorthernMail.Objects.PrivKey;
import com.NorthernSec.NorthernMail.Objects.PubKey;

public class DecryptedMail {
    private final String subject;
    private final String message;
    private final PubKey signedBy;
    private final PrivKey receiver;
    private final Boolean wasEncrypted;
    
    public DecryptedMail(String subj, String mess, PubKey signedBy, PrivKey receiver, Boolean encrypted){
    	subject=subj;message=mess;this.signedBy=signedBy;this.receiver=receiver;wasEncrypted=encrypted;
    }
    public String getSubject(){return subject;}
    public String getMessage(){return message;}
    public String getSender(){if(signedBy != null){return signedBy.getName();}else{return null;}}
    public PubKey getSenderKey(){return signedBy;}
    public String getReceiver(){return receiver.getName();}
    public PrivKey getReveiverKey(){return receiver;}
    public Boolean wasEncrypted(){return wasEncrypted;}
}
