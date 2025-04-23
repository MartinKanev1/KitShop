import React from 'react';
import '../Styles/Footer.css';


const Footer = ({ showFooter = true }) => {
  if (!showFooter) return null;

  return (
    <footer
      className="footer" 
      
    >
       <p>📧 Email: support@footykits.com</p>
       <p>📞 Phone: +1 234 567 890</p>
    </footer>
  );
};

export default Footer;