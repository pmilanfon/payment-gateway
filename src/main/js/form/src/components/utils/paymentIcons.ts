import visaIcon from '../../assets/visa.svg'; 
import mastercardIcon from '../../assets/mastercard.svg';
import amexIcon from '../../assets/amex.svg';
import dinersIcon from '../../assets/diners.svg';
import discoverIcon from '../../assets/discover.svg';
import jcbIcon from '../../assets/jcb.svg';
import maestroIcon from '../../assets/maestro.svg';

const paymentIcons = [
  visaIcon,
  mastercardIcon,
  amexIcon,
  dinersIcon,
  discoverIcon,
  jcbIcon,
  maestroIcon,
];

export const MethodIcon: Record<string, string> =  {
    'visa': visaIcon,
    'mastercard': mastercardIcon,
    'american-express': amexIcon,
    'diners-club': dinersIcon,
    'discover': discoverIcon,
    'jcb': jcbIcon,
    'maestro': maestroIcon,
  
}

export default paymentIcons;