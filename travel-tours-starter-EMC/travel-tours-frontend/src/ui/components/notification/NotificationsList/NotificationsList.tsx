import { Box, Card, CardContent, Typography, Stack } from '@mui/material';
import type { DisplayNotificationResponse } from '../../../../api/types/notification.ts';

interface NotificationsListProps {
  notifications: DisplayNotificationResponse[];
}

const NotificationsList = ({ notifications }: NotificationsListProps) => {
  if (notifications.length === 0) {
    return (
      <Box className='empty-notifications'>
        <Typography variant='body1' color='text.secondary'>
          You don't have any notifications yet.
        </Typography>
      </Box>
    );
  }

  return (
    <Stack spacing={2}>
      {notifications.map((notification) => (
        <Card key={notification.id} className='notification-item' data-id={notification.id}>
          <CardContent>
            <Typography variant='body1' className='notification-message'>
              {notification.message}
            </Typography>
            <Typography variant='caption' color='text.secondary' className='notification-created-at'>
              {notification.createdAt}
            </Typography>
          </CardContent>
        </Card>
      ))}
    </Stack>
  );
};

export default NotificationsList;
