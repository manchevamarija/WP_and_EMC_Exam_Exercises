import { useContext } from 'react';
import ToursContext, { type ToursContextType } from '../contexts/toursContext.ts';

const useTours = () => useContext<ToursContextType>(ToursContext);

export default useTours;